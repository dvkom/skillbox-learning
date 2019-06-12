package numberGenerators.producerConsumer;

import numberGenerators.Generatable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class producerConsumerNumberGenerator implements Generatable {
  private static final char[] LETTERS =
      {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
  private static final int MAX_NUMBER_VALUE = 1000;
  private static final int MAX_REGION_CODE = 50;
  private static final int BUFFER_SIZE = 10_000;
  private static final String PATH_TO_SAVE = "src/main/res/numbers_notVeryOptimized.txt";
  public static final int THREAD_COUNTS = 4;
  public static final int QUEUE_SIZE = 2000;
  private BlockingQueue<String> numbersQueue;

  @Override
  public void generate() {
    numbersQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNTS);
    for (int i = 0; i < THREAD_COUNTS; i++) {
      executor.submit(new Producer(i * MAX_NUMBER_VALUE / THREAD_COUNTS + 1,
          MAX_NUMBER_VALUE / THREAD_COUNTS));
    }
    executor.shutdown();

    Thread consumerThread = new Thread(new Consumer());
    consumerThread.start();
    try {
      try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException ex) {
        executor.shutdownNow();
      }
      consumerThread.interrupt();
      consumerThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  class Producer implements Runnable {
    private int start;
    private int offset;

    public Producer(int start, int offset) {
      this.start = start;
      this.offset = offset;
    }

    @Override
    public void run() {
      System.out.println("Producer started");
      StringBuilder carNumbers = new StringBuilder();
      for (int number = start; number < start + offset; number++) {
        for (int regionCode = 1; regionCode < MAX_REGION_CODE; regionCode++) {
          for (char firstLetter : LETTERS) {
            for (char secondLetter : LETTERS) {
              for (char thirdLetter : LETTERS) {
                if (carNumbers.length() > BUFFER_SIZE) {
                  try {
                    numbersQueue.put(carNumbers.toString());
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    return;
                  }
                  carNumbers = new StringBuilder();
                }
                carNumbers.append(firstLetter);
                if (number < 10) {
                  carNumbers.append("00");
                } else if (number < 100) {
                  carNumbers.append("0");
                }
                carNumbers.append(number)
                    .append(secondLetter)
                    .append(thirdLetter);
                if (regionCode < 10) {
                  carNumbers.append("0");
                }
                carNumbers.append(regionCode)
                    .append("\n");
              }
            }
          }
        }
      }
      try {
        numbersQueue.put(carNumbers.toString());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Producer done");
    }
  }

  class Consumer implements Runnable {

    @Override
    public void run() {
      System.out.println("Consumer started");
      try (FileOutputStream s = new FileOutputStream(PATH_TO_SAVE)) {
        while (!Thread.interrupted()) {
          ArrayList<String> objects = new ArrayList<>();
          numbersQueue.drainTo(objects);

          long charsSent = 0;
          long start = System.nanoTime();
          for (String object : objects) {
            s.write(object.getBytes());
            charsSent += object.length();
          }
          s.flush();
          System.out.printf("took %d objects (%.2f MB/s)%n", objects.size(),
              charsSent / 1024 / 1024.0 / ((System.nanoTime() - start) / 1_000_000_000.0));
        }

        ArrayList<String> objects = new ArrayList<>();
        numbersQueue.drainTo(objects);
        System.out.println("Almost done " + objects.size());
        for (String object : objects) {
          s.write(object.getBytes());
        }

        System.out.println("Consumer done");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
