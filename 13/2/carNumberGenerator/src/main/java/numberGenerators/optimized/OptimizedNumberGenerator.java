package numberGenerators.optimized;

import numberGenerators.Generatable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OptimizedNumberGenerator implements Generatable {
  private static final int PARTS = 10;
  private static final int MAX_NUMBER_VALUE = 1000;
  static final int STEP = MAX_NUMBER_VALUE / PARTS;

  @Override
  public void generate() {
    ExecutorService executor = Executors.newFixedThreadPool(PARTS);
    for (int part = 1; part < MAX_NUMBER_VALUE; part += STEP) {
      executor.submit(new WriterTask(part));
    }
    executor.shutdown();
    try {
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      executor.shutdownNow();
    }

  }
}
