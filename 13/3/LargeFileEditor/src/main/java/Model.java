import java.nio.file.Path;

public interface Model {
  void setPathToFile(Path pathToFile);
  String openFile();
  String readSnippet(int lineNumber);
  int getLinesCount();
}
