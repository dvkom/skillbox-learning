package contracts;

import java.nio.file.Path;

public interface SearchUrlsContract {

  interface View extends BaseView<Presenter> {
    void setPresenter(Presenter presenter);
    String getStartUrl();
    String getPauseValue();
    Path getPathToSaveResult();
    void showStatistic(String processedUrls, String unprocessedUrls);
    void showStopwatch(String time);
    void showReadyView();
    void showNotReadyView();
    void showStartedView();
    void showStoppedView();
    void showSuspendState();
    void showResumeState();
    void showProgress();
    void hideProgress();
    void showFileWritingErrorMessage();
    void showFileWritingSuccessMessage();
  }

  interface Presenter extends BasePresenter {
    void onUrlTyped();
    void onStartSearch();
    void onStopSearch();
    void onPauseSearch();
  }

  interface Model extends Runnable {
    int getProcessedUrlsCount();
    int getUnprocessedUrlsCount();
    String getStopwatchTime();
    void suspend();
    void resume();
    boolean isSuspended();
    void setVisitPause(long pause);
    boolean saveResultToFile(Path pathToSaveResult);
  }
}
