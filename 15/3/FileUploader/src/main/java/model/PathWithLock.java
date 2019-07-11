package model;

import java.nio.file.Path;
import java.util.concurrent.locks.ReadWriteLock;

public class PathWithLock {
  private final Path path;
  private final ReadWriteLock readWriteLock;

  public PathWithLock(Path path, ReadWriteLock readWriteLock) {
    this.path = path;
    this.readWriteLock = readWriteLock;
  }

  public Path getPath() {
    return path;
  }

  public ReadWriteLock getReadWriteLock() {
    return readWriteLock;
  }
}
