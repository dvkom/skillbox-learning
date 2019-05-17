package models;

import contracts.SearchUrlsContract;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngineModel implements SearchUrlsContract.Model {
  private static final Logger log = Logger.getLogger(SearchEngineModel.class);
  private String startUrl;
  private Set<String> processedUrls = new HashSet<>();
  private Set<String> unprocessedUrls = new HashSet<>();
  private volatile int processedUrlsCount = 0;
  private volatile int unprocessedUrlsCount = 0;
  private volatile AtomicInteger millis;
  private volatile AtomicInteger minutes;
  private static final long PAUSE = 20;
  private static final int THREAD_COUNT = 40;
  private volatile boolean isSuspended = false;
  private int startUrlSlashCount;
  private Thread stopwatchThread;

  public SearchEngineModel(String startUrl) {
    this.startUrl = normalize(startUrl);
    startUrlSlashCount = getSlashCount(this.startUrl);
    millis = new AtomicInteger(0);
    minutes = new AtomicInteger(0);
  }

  @Override
  public void run() {
    log.info("URLs search was started");
    createAndStartStopwatchThread();
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    unprocessedUrls.add(startUrl);
    Set<Future<Set<String>>> futures = new HashSet<>();

    while (!unprocessedUrls.isEmpty() || !futures.isEmpty()) {
      int freeThreads = THREAD_COUNT - futures.size();

      Set<String> urlsToProcessing = unprocessedUrls
          .stream()
          .limit(freeThreads)
          .collect(Collectors.toSet());
      unprocessedUrls.removeAll(urlsToProcessing);

      futures.addAll(urlsToProcessing
          .stream()
          .map(SearchTask::new)
          .map(executor::submit)
          .collect(Collectors.toSet())
      );

      Set<Future<Set<String>>> doneFutures = futures
          .stream()
          .filter(Future::isDone)
          .collect(Collectors.toSet());
      futures.removeAll(doneFutures);

      try {
        TimeUnit.MILLISECONDS.sleep(PAUSE);
        while (isSuspended) {
          // Если на момент паузы в пуле остались незавершенные таски, они выполнятся.
          // При этом новые такси в пул не попадут, за счет чего и достигается пауза.
          TimeUnit.MILLISECONDS.sleep(100);
        }
      } catch (InterruptedException e) {
        log.info("URLs search was interrupted");
        executor.shutdownNow();
        break;
      }

      try {
        for (Future<Set<String>> future : doneFutures) {
          Set<String> result = future.get();
          if (result != null) {
            result
                .stream()
                .filter(url -> !processedUrls.contains(url))
                .forEach(url -> {
                  processedUrls.add(url);
                  if (isNotFileUrl(url)) {
                    unprocessedUrls.add(url);
                  }
                });
          }
        }
      } catch (InterruptedException | ExecutionException e) {
        log.error(e);
        continue;
      }

      processedUrlsCount = processedUrls.size();
      unprocessedUrlsCount = unprocessedUrls.size() + futures.size();
    }
    log.info("URLS search was finished");

    try {
      if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      executor.shutdownNow();
    }
    processedUrls = processedUrls.stream()
        .map(this::normalize)
        .map(this::alignWithSpaces)
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toCollection(LinkedHashSet::new));

    log.info("URLs were collected");
  }

  private void createAndStartStopwatchThread() {
    stopwatchThread = new Thread(() -> {
      while (true) {
        millis.incrementAndGet();
        if (millis.get() >= 6_000) {
          minutes.incrementAndGet();
          millis.set(0);
        }
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          log.debug("Stopwatch was interrupted");
          break;
        }
      }
    });
    stopwatchThread.start();
  }

  @Override
  public boolean saveResultToFile(Path pathToSaveResult) {
    try {
      if (processedUrls != null) {
        log.info("Started saving to the file: " + pathToSaveResult);
        Files.write(pathToSaveResult, processedUrls);
      }
    } catch (IOException e) {
      log.error("Failed saving to the file");
      return false;
    }
    log.info("Finished saving to the file: " + pathToSaveResult);
    return true;
  }

  @Override
  public void setVisitPause(long pause) {
    SearchTask.setPause(pause);
  }

  private String normalize(String s) {
    if (isNotFileUrl(s) && !s.endsWith("/")) {
      s += "/";
    }
    return s;
  }

  private int getSlashCount(String s) {
    return (int) s.chars().filter(ch -> ch == '/').count();
  }

  private String alignWithSpaces(String s) {
    int slashCount = getSlashCount(s) - startUrlSlashCount;
    return "   ".repeat(slashCount) + s;
  }

  private boolean isNotFileUrl(String url) {
    Pattern fileFilter = Pattern.compile(
        ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    return !fileFilter.matcher(url).matches();
  }

  @Override
  public int getProcessedUrlsCount() {
    return processedUrlsCount;
  }

  @Override
  public int getUnprocessedUrlsCount() {
    return unprocessedUrlsCount;
  }

  @Override
  public String getStopwatchTime() {
    return String.format(
        "%d:%02d:%02d", minutes.get(), millis.get() / 100 % 100, millis.get() % 100
    );
  }

  @Override
  public void suspend() {
    log.info("URLs search was suspended");
    isSuspended = true;
    if (!stopwatchThread.isInterrupted()) {
      stopwatchThread.interrupt();
    }
  }

  @Override
  public void resume() {
    log.info("URLs search was resumed");
    isSuspended = false;
    createAndStartStopwatchThread();
  }

  @Override
  public boolean isSuspended() {
    return isSuspended;
  }
}
