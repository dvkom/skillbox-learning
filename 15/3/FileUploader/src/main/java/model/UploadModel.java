package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface UploadModel {
  void writeFile(String fileName, InputStream inputStream) throws IOException;
  boolean contains(String fileName);
  void loadFile(String fileName, OutputStream outputStream) throws IOException;
  List<String> getAllFileNames();
}
