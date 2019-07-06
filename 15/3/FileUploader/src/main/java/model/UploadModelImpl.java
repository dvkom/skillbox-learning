package model;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ManagedBean
public class UploadModelImpl implements UploadModel {
  private static final Path DEFAULT_PATH_TO_SAVE = Path.of("D:\\");
  private static final int BUFFER_SIZE = 10240;
  private static volatile CopyOnWriteArrayList<Path> storage = new CopyOnWriteArrayList<>();

  @Override
  public void writeFile(Part filePart, String pathToUpload) throws IOException {
    Path pathToSave = Path.of(pathToUpload);
    if (!Files.exists(pathToSave)) {
      pathToSave = DEFAULT_PATH_TO_SAVE;
    }
    String fileName = filePart.getSubmittedFileName();
    Path pathToFile = pathToSave.resolve(fileName);
    storage.addIfAbsent(pathToFile);

    try (InputStream inputStream = filePart.getInputStream();
         OutputStream outputStream = Files.newOutputStream(pathToFile)) {

      byte[] buffer = new byte[BUFFER_SIZE];
      while (inputStream.available() >= BUFFER_SIZE) {
        inputStream.read(buffer);
        outputStream.write(buffer);
      }
      buffer = new byte[inputStream.available()];
      inputStream.read(buffer);
      outputStream.write(buffer);
    }
  }

  @Override
  public boolean contains(Path pathToFile) {
    return storage.contains(pathToFile);
  }

  @Override
  public void loadFile(Path pathToFile, HttpServletResponse response) throws IOException {
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Content-disposition",
        "attachment; filename=" + pathToFile.getFileName());

    try(InputStream in = Files.newInputStream(pathToFile);
        OutputStream out = response.getOutputStream()) {

      byte[] buffer = new byte[BUFFER_SIZE];
      int numBytesRead;
      while ((numBytesRead = in.read(buffer)) > 0) {
        out.write(buffer, 0, numBytesRead);
      }
    }
  }

  @Override
  public List<Path> getAllPaths() {
    return storage;
  }
}
