package presenters;

import contracts.Model;
import contracts.SearchUrlsContract;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Presenter implements SearchUrlsContract.Presenter {
  private static final Logger log = Logger.getLogger(Presenter.class);
  private Thread updaterThread;
  private SearchUrlsContract.View view;
  private Model model;

  public Presenter(Model model, SearchUrlsContract.View view) {
    this.view = view;
    this.model = model;
  }

  @Override
  public void onUrlTyped() {
    String url = view.getStartUrl();
    if (isValidUrl(url)) {
      view.showReadyView();
    } else {
      view.showNotReadyView();
    }
  }

  @Override
  public void onStartSearch() {
    String pauseCount = view.getPauseValue().replaceAll("[^\\d]", "");
    model.setVisitPause(Long.parseLong(pauseCount));
    model.start(view.getStartUrl());
    view.showStartedView();
    view.showProgress();
    createAndStartUpdaterThread();
  }

  @Override
  public void onStopSearch() {
    model.stop();
  }

  @Override
  public void onPauseSearch() {
    if (model.isSuspended()) {
      model.resume();
      view.showResumeState();
      view.showProgress();
    } else {
      model.suspend();
      view.showSuspendState();
      view.hideProgress();
    }
  }

  private void createAndStartUpdaterThread() {
    updaterThread = new Thread(() -> {
      log.debug("The Update Thread was started");
      while (true) {
        String processedUrlsCount = String.valueOf(model.getProcessedUrlsCount());
        String unprocessedUrlsCount = String.valueOf(model.getUnprocessedUrlsCount());
        String stopwatchTime = model.getStopwatchTime();
        view.showStatistic(processedUrlsCount, unprocessedUrlsCount);
        view.showStopwatch(stopwatchTime);
        try {
          if (model.isSearchDone()) {
            boolean isSuccess = model.saveResultToFile(view.getPathToSaveResult());
            view.showStoppedView();
            view.hideProgress();
            if (isSuccess) {
              view.showFileWritingSuccessMessage();
            } else {
              view.showFileWritingErrorMessage();
            }
            updaterThread.interrupt();
          }
          TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
          log.debug("The Update Thread was interrupted");
          break;
        }
      }
    });
    updaterThread.start();
  }

  private boolean isValidUrl(String url) {
    String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    try {
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(url);
      return matcher.matches();
    } catch (RuntimeException e) {
      return false;
    }
  }
}
