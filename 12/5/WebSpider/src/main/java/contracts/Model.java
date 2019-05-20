package contracts;

import java.nio.file.Path;

public interface Model {
    int getProcessedUrlsCount();
    int getUnprocessedUrlsCount();
    String getStopwatchTime();
    void start(String startUrl);
    void stop();
    void suspend();
    void resume();
    boolean isSuspended();
    boolean isSearchDone();
    void setVisitPause(long pause);
    boolean saveResultToFile(Path pathToSaveResult);
}
