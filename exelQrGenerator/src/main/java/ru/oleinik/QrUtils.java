package ru.oleinik;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилитный класс для генерации QR-кодов
 */
public class QrUtils {

    /**
     * Генерирует QR-код по тексту и возвращает как PNG в виде массива байтов
     *
     * @param text текст для QR
     * @param size размер QR-кода в пикселях
     * @return массив байтов PNG
     * @throws WriterException если не удалось сгенерировать QR
     * @throws IOException     если ошибка при записи в поток
     */
    public static byte[] generateQr(String text, int size) throws WriterException, IOException {
        // Создаём объект генератора QR
        QRCodeWriter writer = new QRCodeWriter();

        // Настройки QR-кода
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1); // минимальные поля

        // Генерируем матрицу QR-кода
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);

        // Преобразуем матрицу в PNG
        ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOut);

        // Возвращаем массив байтов изображения
        return pngOut.toByteArray();
    }
}
