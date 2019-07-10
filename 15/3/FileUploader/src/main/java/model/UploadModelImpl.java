package model;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
public class UploadModelImpl implements UploadModel {
  private static volatile ConcurrentHashMap<String, Path> storage = new ConcurrentHashMap<>();

  @Override
  public void writeFile(String fileName, InputStream inputStream) throws IOException {
    Path pathToFile = Files.createTempFile(fileName, "tmp");
    pathToFile.toFile().deleteOnExit();

    try (OutputStream outputStream = Files.newOutputStream(pathToFile)) {
      inputStream.transferTo(outputStream);
      storage.put(fileName, pathToFile);
    }
  }

  @Override
  public boolean contains(String fileName) {
    return storage.containsKey(fileName);
  }

  @Override
  public void loadFile(String fileName, OutputStream outputStream) throws IOException {

    try (InputStream inputStream = Files.newInputStream(storage.get(fileName))) {
      inputStream.transferTo(outputStream);
    }
  }

  @Override
  public List<String> getAllFileNames() {
    return new ArrayList<>(storage.keySet());
  }
}
