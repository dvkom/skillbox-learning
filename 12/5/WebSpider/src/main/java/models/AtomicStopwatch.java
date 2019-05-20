package models;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicStopwatch {
  private AtomicLong startTime;
  private AtomicLong startPauseTime = new AtomicLong();
  private AtomicLong pausedTime = new AtomicLong();
  private AtomicBoolean isPaused;

  public void start() {
    startTime = new AtomicLong(System.nanoTime());
    pausedTime.set(0);
    isPaused = new AtomicBoolean(false);
  }

  public void pause() {
    isPaused.set(true);
    startPauseTime.set(System.nanoTime());
  }

  public void resume() {
    isPaused.set(false);
    pausedTime.addAndGet(System.nanoTime() - startPauseTime.get());
  }

  public long getElapsedTime() {
    return isPaused.get() ?
        (startPauseTime.get() - startTime.get() - pausedTime.get()) / 10_000_000 :
        (System.nanoTime() - startTime.get() - pausedTime.get()) / 10_000_000;
  }
}