import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ViewerPresenter implements ViewerContract.Presenter {
  private static final Logger log = Logger.getLogger(ViewerModel.class);
  private Model model;
  private ViewerContract.View view;
  private String currentSnippet;
  private volatile boolean isFileOpened = false;
  private Thread viewerUpdater;

  private volatile int currentScrollPosition;
  private volatile int lastScrollPosition;

  public ViewerPresenter(Model model, ViewerContract.View view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void onOpenFile(Path pathToFile) {
    if (viewerUpdater != null && !viewerUpdater.isInterrupted()) {
      viewerUpdater.interrupt();
    }
    isFileOpened = false;
    model.setPathToFile(pathToFile);
    createAndStartUpdateThread();
  }

  private void createAndStartUpdateThread() {
    viewerUpdater = new Thread(() -> {
      while (true) {
        if (isFileOpened) {
          currentScrollPosition = view.getCurrentScrollPosition();
          if (lastScrollPosition != currentScrollPosition) {
            log.info("Current scroll position = " + currentScrollPosition);
            view.showNotReadyView();
            currentSnippet = model.readSnippet(currentScrollPosition);
            view.setText(Objects.requireNonNullElse(currentSnippet, ""));
            view.showReadyView();
            view.setCurrentLineNumber(currentScrollPosition);
            lastScrollPosition = currentScrollPosition;
          }
        } else {
          view.showNotReadyView();
          currentSnippet = model.openFile();
          view.setText(Objects.requireNonNullElse(currentSnippet, ""));
          view.setMaximumLine(model.getLinesCount());
          view.setScrollPosition(0);
          view.setCurrentLineNumber(0);
          lastScrollPosition = 0;
          view.showReadyView();
          isFileOpened = true;
        }
        try {
          TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
          log.error(e);
          break;
        }
      }
    });
    viewerUpdater.start();
  }
}
