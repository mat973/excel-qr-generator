package ru.oleinik;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MultiSheetQrGeneratorXlsmFinalWithPadding {

    public static void main(String[] args) {
        String inputPath = "test.xlsm";
        String outputPath = "output_with_qr.xlsm";

        final int EMU_PER_PIXEL = 9525;  // 1 пиксель ≈ 9525 EMU
        final int PADDING = 4;           // пикселя отступ сверху и слева

        try (OPCPackage pkg = OPCPackage.open(new FileInputStream(inputPath));
             XSSFWorkbook workbook = new XSSFWorkbook(pkg)) {

            CreationHelper helper = workbook.getCreationHelper();

            for (int s = 1; s < workbook.getNumberOfSheets(); s++) { // пропускаем первый лист
                Sheet sheet = workbook.getSheetAt(s);
                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

                // Находим строку с заголовками
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

                int linkCol = -1, qrCol = -1;
                for (Cell cell : headerRow) {
                    String value = cell.toString().trim();
                    if ("Ссылка на счёт".equalsIgnoreCase(value)) linkCol = cell.getColumnIndex();
                    if ("QR код".equalsIgnoreCase(value)) qrCol = cell.getColumnIndex();
                }
                if (linkCol == -1 || qrCol == -1) continue;

                for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) row = sheet.createRow(i);

                    Cell linkCell = row.getCell(linkCol);
                    if (linkCell == null) continue;

                    String link = linkCell.toString().trim();
                    if (link.isEmpty()) continue;

                    try {
                        // Вычисляем размеры ячейки в пикселях
                        int colWidthPx = sheet.getColumnWidth(qrCol) * 7 / 256;
                        int rowHeightPx = (int) (row.getHeightInPoints() * 1.33);

                        int qrSizePx = Math.min(colWidthPx, rowHeightPx) - PADDING * 2;
                        if (qrSizePx <= 0) qrSizePx = 10;

                        // Генерация QR
                        byte[] qrBytes = QrUtils.generateQr(link, qrSizePx);
                        int picIdx = workbook.addPicture(qrBytes, Workbook.PICTURE_TYPE_PNG);

                        // Настраиваем размеры ячейки
                        row.setHeightInPoints(rowHeightPx / 1.33f);
                        sheet.setColumnWidth(qrCol, colWidthPx * 256 / 7);

                        // Создаем якорь
                        ClientAnchor anchor = helper.createClientAnchor();
                        anchor.setCol1(qrCol);
                        anchor.setRow1(i);
                        anchor.setCol2(qrCol + 1);
                        anchor.setRow2(i + 1);
                        anchor.setDx1(PADDING * EMU_PER_PIXEL); // отступ слева
                        anchor.setDy1(PADDING * EMU_PER_PIXEL); // отступ сверху
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

                        XSSFPicture pic = drawing.createPicture(anchor, picIdx);

                    } catch (Exception e) {
                        System.out.println("⚠️ Не удалось сгенерировать QR для строки " + (i + 1));
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
            }

            System.out.println("✅ Все листы обработаны. Файл сохранён как " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
