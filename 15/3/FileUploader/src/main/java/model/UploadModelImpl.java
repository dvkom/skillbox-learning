package model;

import javax.annotation.ManagedBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ManagedBean
public class UploadModelImpl implements UploadModel {
  private static volatile ConcurrentHashMap<String, PathWithLock> storage = new ConcurrentHashMap<>();

  @Override
  public void writeFile(String fileName, InputStream inputStream) throws IOException {

    final Path pathToFile;
    final ReadWriteLock readWriteLock;
    final PathWithLock pathWithLock = storage.get(fileName);

    if (pathWithLock != null) {
      pathToFile = pathWithLock.getPath();
      readWriteLock = pathWithLock.getReadWriteLock();
    } else {
      pathToFile = Files.createTempFile(fileName, "tmp");
      pathToFile.toFile().deleteOnExit();
      readWriteLock = new ReentrantReadWriteLock();
    }

    readWriteLock.writeLock().lock();
    storage.put(fileName, new PathWithLock(pathToFile, readWriteLock));

    try (OutputStream outputStream = Files.newOutputStream(pathToFile)) {
      inputStream.transferTo(outputStream);
    } catch (IOException e) {
      storage.remove(fileName);
      throw e;
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public boolean contains(String fileName) {
    return storage.containsKey(fileName);
  }

  @Override
  public void loadFile(String fileName, OutputStream outputStream) throws IOException {

    final PathWithLock pathWithLock = storage.get(fileName);

    if (pathWithLock != null) {
      pathWithLock.getReadWriteLock().readLock().lock();
      try (InputStream inputStream = Files.newInputStream(pathWithLock.getPath())) {
        inputStream.transferTo(outputStream);
      } finally {
        pathWithLock.getReadWriteLock().readLock().unlock();
      }
    }
  }

  @Override
  public List<String> getAllFileNames() {
    return new ArrayList<>(storage.keySet());
  }
}
