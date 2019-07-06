package model;

public interface SingleObjectStorage<T> {
  void save(T object);
  T get();
}
