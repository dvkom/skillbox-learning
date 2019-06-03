import java.nio.file.Path;

public interface ViewerContract {
  interface View {
    void setPresenter(Presenter presenter);
    void setText(String text);
    void showReadyView();
    void showNotReadyView();
    int getCurrentScrollPosition();
    void setScrollPosition(int position);
    void setMaximumLine(int maximumLine);
    void setCurrentLineNumber(int lineNumber);
  }

  interface Presenter {
    void onOpenFile(Path pathToFile);
  }
}
