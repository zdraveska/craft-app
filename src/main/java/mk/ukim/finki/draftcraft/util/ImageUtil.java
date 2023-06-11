package mk.ukim.finki.draftcraft.util;

import mk.ukim.finki.draftcraft.domain.exceptions.ImageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {

  public static byte[] compressMultipartFile(MultipartFile multipartFile) {
    byte[] data;
    try {
      data = multipartFile.getBytes();
    } catch (IOException e) {
      throw new ImageException();
    }
    Deflater deflater = new Deflater();
    deflater.setInput(data);
    deflater.finish();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[500000];
    while (!deflater.finished()) {
      int count = deflater.deflate(buffer);
      outputStream.write(buffer, 0, count);
    }
    try {
      outputStream.close();
    } catch (IOException e) {
      throw new ImageException();
    }
    return outputStream.toByteArray();
  }

  public static byte[] decompressBytes(byte[] data) {
    if (data == null) {
      return null;
    }
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    try {
      while (!inflater.finished()) {
        int count = inflater.inflate(buffer);
        outputStream.write(buffer, 0, count);
      }
      outputStream.close();
    } catch (IOException | DataFormatException ignored) {
    }
    return outputStream.toByteArray();
  }

  public static void checkIfImageIsValid(MultipartFile file) {
    if (file.getContentType() != null
        && !(file.getContentType().endsWith("jpg")
        || file.getContentType().endsWith("png")
        || file.getContentType().endsWith("jpeg"))) {
      throw new ImageException(file.getContentType());
    }
  }

}
