//package ru.oleinik;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//
///**
// * Программа читает ссылки из первого столбца Excel
// * и генерирует QR-коды напротив каждой ссылки во втором столбце.
// */
//public class Main {
//    public static void main(String[] args) {
//        // Путь к входному Excel файлу (ссылки в первом столбце)
//        String inputPath = "data.xlsx";
//
//        // Путь к выходному файлу (куда вставим QR)
//        String outputPath = "output_with_qr.xlsx";
//
//        // Попытка открыть файл и обработать Excel
//        try (FileInputStream fis = new FileInputStream(inputPath);
//             Workbook workbook = new XSSFWorkbook(fis)) { // открываем XLSX файл
//
//            // Берём первый лист (0-й индекс)
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Объект-помощник для создания изображений и других элементов
//            CreationHelper helper = workbook.getCreationHelper();
//
//            // "Холст", куда будем вставлять картинки
//            Drawing<?> drawing = sheet.createDrawingPatriarch();
//
//            int qrSize = 150; // размер QR-кода в пикселях
//
//            // Проходим по всем строкам листа
//            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i); // берём строку
//                if (row == null) continue; // если строка пустая — пропускаем
//
//                Cell linkCell = row.getCell(0); // первая колонка (A)
//                if (linkCell == null) continue; // если ячейка пустая — пропускаем
//
//                String link = linkCell.toString().trim(); // получаем текст ссылки
//                if (link.isEmpty()) continue; // если текст пустой — пропускаем
//
//                // Генерация QR-кода из ссылки
//                byte[] qrBytes = QrUtils.generateQr(link, qrSize);
//
//                // Добавляем картинку в книгу Excel
//                int pictureIdx = workbook.addPicture(qrBytes, Workbook.PICTURE_TYPE_PNG);
//
//                // Настройка позиции картинки в таблице
//                ClientAnchor anchor = helper.createClientAnchor();
//                anchor.setCol1(1); // колонка B (вторая) для QR
//                anchor.setRow1(i); // та же строка, что и ссылка
//                anchor.setCol2(2); // до колонки C (ширина картинки)
//                anchor.setRow2(i + 1); // до следующей строки (высота картинки)
//                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE); // чтобы картинка двигалась с ячейкой
//
//                // Вставляем картинку на лист
//                drawing.createPicture(anchor, pictureIdx);
//
//                // Увеличиваем высоту строки, чтобы QR-код полностью помещался
//                row.setHeightInPoints(100);
//            }
//
//            // Устанавливаем ширину колонки с QR-кодами
//            sheet.setColumnWidth(1, 25 * 256);
//
//            // Сохраняем результат в новый файл
//            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
//                workbook.write(fos);
//            }
//
//            System.out.println("✅ Готово! Файл сохранён как " + outputPath);
//
//        } catch (Exception e) {
//            e.printStackTrace(); // выводим ошибку, если что-то пошло не так
//        }
//    }
//}
