package model;

import javax.servlet.http.Part;
import java.io.IOException;

public interface UploadModel {
  void writeFile(Part filePart, String pathToUpload) throws IOException;
}
