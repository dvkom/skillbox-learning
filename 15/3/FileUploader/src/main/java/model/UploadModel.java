package model;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface UploadModel {
  void writeFile(Part filePart, String pathToUpload) throws IOException;
  boolean contains(Path pathToFile);
  void loadFile(Path pathToFile, HttpServletResponse response) throws IOException;
  List<Path> getAllPaths();
}
