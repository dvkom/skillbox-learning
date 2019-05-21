public class SynchronizedStopwatch {
  private long startTime;
  private long startPauseTime;
  private long pausedTime;
  private boolean isPaused;

  public synchronized void start() {
    startTime = System.nanoTime();
    pausedTime = 0;
    isPaused = false;
  }

  public synchronized void pause() {
    isPaused = true;
    startPauseTime = System.nanoTime();
  }

  public synchronized void resume() {
    isPaused = false;
    pausedTime += System.nanoTime() - startPauseTime;
  }

  public synchronized long getElapsedTime() {
    return isPaused ?
        (startPauseTime - startTime - pausedTime) / 10_000_000 :
        (System.nanoTime() - startTime - pausedTime) / 10_000_000;
  }
}
