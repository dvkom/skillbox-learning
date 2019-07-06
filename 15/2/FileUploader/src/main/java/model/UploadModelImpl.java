package model;

import org.apache.log4j.Logger;

import javax.annotation.ManagedBean;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@ManagedBean
public class UploadModelImpl implements UploadModel {
  private static final Logger log = Logger.getLogger(UploadModelImpl.class);
  private static final Path DEFAULT_PATH_TO_SAVE = Path.of("D:\\");
  private static final int BUFFER_SIZE = 10240;

  @Override
  public void writeFile(Part filePart, String pathToUpload) throws IOException {
    Path pathToSave = Path.of(pathToUpload);
    if (!Files.exists(pathToSave)) {
      pathToSave = DEFAULT_PATH_TO_SAVE;
    }

    String fileName = filePart.getSubmittedFileName();
    try (InputStream inputStream = filePart.getInputStream();
         OutputStream outputStream = new FileOutputStream(pathToSave.resolve(fileName).toString())) {

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
}
