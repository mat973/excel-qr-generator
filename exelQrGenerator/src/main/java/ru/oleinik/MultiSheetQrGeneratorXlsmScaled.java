package ru.oleinik;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFPicture;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Генератор QR-кодов для всех листов Excel (.xlsm)
 * QR масштабируется под размер ячейки, не перекрывая границы.
 */
public class MultiSheetQrGeneratorXlsmScaled {

    public static void main(String[] args) {
        String inputPath = "test.xlsm";
        String outputPath = "output_with_qr.xlsm";
        int qrSize = 150; // размер QR при генерации (в пикселях)

        try (OPCPackage pkg = OPCPackage.open(new FileInputStream(inputPath));
             XSSFWorkbook workbook = new XSSFWorkbook(pkg)) {

            CreationHelper helper = workbook.getCreationHelper();

            // Проходим по всем листам, кроме первого
            for (int s = 1; s < workbook.getNumberOfSheets(); s++) {
                Sheet sheet = workbook.getSheetAt(s);
                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

                // Ищем строку с заголовками
                Row headerRow = null;
                int headerRowIndex = -1;
                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;

                    boolean foundLink = false;
                    boolean foundQr = false;
                    for (Cell cell : row) {
                        String value = cell.toString().trim();
                        if ("Ссылка на счёт".equalsIgnoreCase(value)) foundLink = true;
                        if ("QR код".equalsIgnoreCase(value)) foundQr = true;
                    }
                    if (foundLink && foundQr) {
                        headerRow = row;
                        headerRowIndex = r;
                        break;
                    }
                }

                if (headerRow == null) continue;

                // Находим индексы колонок
                int linkCol = -1, qrCol = -1;
                for (Cell cell : headerRow) {
                    String value = cell.toString().trim();
                    if ("Ссылка на счёт".equalsIgnoreCase(value)) linkCol = cell.getColumnIndex();
                    if ("QR код".equalsIgnoreCase(value)) qrCol = cell.getColumnIndex();
                }

                if (linkCol == -1 || qrCol == -1) continue;

                // Проходим по строкам ниже заголовка
                for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Cell linkCell = row.getCell(linkCol);
                    if (linkCell == null) continue;

                    String link = linkCell.toString().trim();
                    if (link.isEmpty()) continue;

                    try {
                        // Генерация QR
                        byte[] qrBytes = QrUtils.generateQr(link, qrSize);
                        int pictureIdx = workbook.addPicture(qrBytes, Workbook.PICTURE_TYPE_PNG);

                        // Создаем якорь на ячейку QR
                        ClientAnchor anchor = helper.createClientAnchor();
                        anchor.setCol1(qrCol);
                        anchor.setRow1(i);
                        anchor.setCol2(qrCol + 1);
                        anchor.setRow2(i + 1);
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

                        // Вставка и масштабирование QR под ячейку
                        XSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
                        picture.resize(0.7); // масштаб относительно ячейки (70%)

                        // Настраиваем высоту строки под QR
                        row.setHeightInPoints(qrSize * 0.6f);

                    } catch (Exception e) {
                        System.out.println("⚠️ Не удалось сгенерировать QR для строки " + (i + 1));
                    }
                }

                // Ширина колонки QR немного больше QR
                sheet.setColumnWidth(qrCol, (int) (qrSize * 1.8));
            }

            // Сохраняем результат
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
            }

            System.out.println("✅ Все листы обработаны. Файл сохранён как " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
