package model;

import org.apache.log4j.Logger;

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
  private static final Logger log = Logger.getLogger(UploadModelImpl.class);
  private static final ConcurrentHashMap<String, PathWithLock> storage = new ConcurrentHashMap<>();

  @Override
  public void writeFile(String fileName, InputStream inputStream) throws IOException {

    log.info("Start writeFile method");
    ReadWriteLock firstLock = new ReentrantReadWriteLock();
    firstLock.writeLock().lock();
    log.info("First write lock: " + firstLock);
    try {
      PathWithLock pathWithLock = storage.computeIfAbsent(fileName, key -> create(key, firstLock));
      Path pathToFile = pathWithLock.getPath();
      if (pathToFile != null) {
        ReadWriteLock secondLock = pathWithLock.getReadWriteLock();
        log.info("Second write lock: " + secondLock);
        if (!secondLock.equals(firstLock)) {
          secondLock.writeLock().lock();
          try {
            log.info(String.format(
                "Writing file: %s with the second lock active (the existing file will be overwritten)",
                pathToFile
            ));
            write(fileName, pathToFile, inputStream);
          } finally {
            secondLock.writeLock().unlock();
          }
        } else {
          log.info(String.format(
              "Writing file: %s with first lock active (the new file name found)",
              pathToFile
          ));
          write(fileName, pathToFile, inputStream);
        }
      } else {
        throw new IOException();
      }

    } finally {
      firstLock.writeLock().unlock();
    }

  }

  private PathWithLock create(String fileName, ReadWriteLock lock) {
    Path path;
    try {
      path = Files.createTempFile(fileName, "tmp");
      path.toFile().deleteOnExit();
    } catch (IOException e) {
      path = null;
    }
    return new PathWithLock(path, lock);
  }

  private void write(String fileName, Path pathToFile, InputStream inputStream) throws IOException {
    try (OutputStream outputStream = Files.newOutputStream(pathToFile)) {
      inputStream.transferTo(outputStream);
    } catch (IOException e) {
      storage.remove(fileName);
      throw e;
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
