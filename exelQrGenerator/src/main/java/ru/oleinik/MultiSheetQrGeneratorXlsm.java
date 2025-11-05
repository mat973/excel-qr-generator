package ru.oleinik;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MultiSheetQrGeneratorXlsm {

    public static void main(String[] args) {
        String inputPath = "test.xlsm";
        String outputPath = "output_with_qr2.xlsm";
        int qrSize = 150;

        try (OPCPackage pkg = OPCPackage.open(new FileInputStream(inputPath));
             Workbook workbook = new XSSFWorkbook(pkg)) { // открываем XLSM файл безопасно

            CreationHelper helper = workbook.getCreationHelper();

            // Проходим по всем листам, кроме первого
           // for (int s = 1; s < workbook.getNumberOfSheets(); s++) {
            for (int s = 1; s < 4; s++) {
                Sheet sheet = workbook.getSheetAt(s);
                Drawing<?> drawing = sheet.createDrawingPatriarch();

                Row headerRow = sheet.getRow(2); // 3-я строка с заголовками
                if (headerRow == null) continue;

                int linkCol = -1, qrCol = -1;

                // Находим нужные колонки
                for (Cell cell : headerRow) {
                    String value = cell.toString().trim();
                    if ("Ссылка на счёт".equalsIgnoreCase(value)) {
                        linkCol = cell.getColumnIndex();
                    } else if ("QR код".equalsIgnoreCase(value)) {
                        qrCol = cell.getColumnIndex();
                    }
                }

                if (linkCol == -1 || qrCol == -1) continue;

                // Обрабатываем все строки после заголовка
                for (int i = 3; i <= sheet.getLastRowNum(); i++) {
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

                        ClientAnchor anchor = helper.createClientAnchor();
                        anchor.setCol1(qrCol);
                        anchor.setRow1(i);
                        anchor.setCol2(qrCol + 1);
                        anchor.setRow2(i + 1);
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

                        drawing.createPicture(anchor, pictureIdx);
                        row.setHeightInPoints(100);
                    } catch (Exception e) {
                        System.out.println("⚠️ Не удалось сгенерировать QR для строки " + (i + 1));
                    }
                }

                sheet.setColumnWidth(qrCol, 25 * 256);
            }

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
            }

            System.out.println("✅ Файл обработан и сохранён: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
