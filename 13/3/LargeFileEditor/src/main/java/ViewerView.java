import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Path;

public class ViewerView implements ViewerContract.View {
  private final static int VISIBLE_LINES_COUNT = 32;
  private JFrame mainFrame;
  private JPanel rootPanel;
  private JTextField fileField;
  private JButton openButton;
  private JTextArea fileTextArea;
  private JScrollBar scrollBar;
  private JProgressBar progressBar;
  private JScrollPane scrollPane;
  private JLabel currentLineNumberLabel;
  private JLabel infoLabel;
  private JFileChooser fileChooser;

  private Path pathToFile;
  private ViewerContract.Presenter presenter;

  public ViewerView() {
    mainFrame = new JFrame();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    mainFrame.setTitle("Large File Editor");
    mainFrame.setSize(800, 625);
    mainFrame.setResizable(false);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mainFrame.setContentPane(rootPanel);
    fileTextArea.setEditable(false);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    mainFrame.setVisible(true);

    initFileChooser();
  }

  private void initFileChooser() {
    UIManager.put("FileChooser.saveButtonText", "Сохранить");
    UIManager.put("FileChooser.cancelButtonText", "Отмена");
    UIManager.put("FileChooser.fileNameLabelText", "Наименование файла");
    UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов");
    UIManager.put("FileChooser.lookInLabelText", "Директория");
    UIManager.put("FileChooser.saveInLabelText", "Сохранить в директории");
    UIManager.put("FileChooser.folderNameLabelText", "Путь директории");
    fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Открыть файл");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Текстовые файлы", "txt", "log", "info", "xml", "html");
    fileChooser.setFileFilter(filter);
  }

  @Override
  public void setPresenter(ViewerContract.Presenter presenter) {
    this.presenter = presenter;
    setListeners();
  }

  private void setListeners() {
    openButton.addActionListener(e -> onOpenButtonPressed());
  }

  @Override
  public void setMaximumLine(int maximumLine) {
    SwingUtilities.invokeLater(() -> {
      scrollBar.setMaximum(maximumLine - 1);
      scrollBar.setVisibleAmount(VISIBLE_LINES_COUNT);
      scrollBar.setMinimum(0);
    });
  }

  @Override
  public void setText(String text) {
    SwingUtilities.invokeLater(() -> fileTextArea.setText(text));
  }

  @Override
  public void showReadyView() {
    SwingUtilities.invokeLater(() -> {
      fileTextArea.setEditable(true);
      progressBar.setIndeterminate(false);
    });
  }

  @Override
  public void showNotReadyView() {
    SwingUtilities.invokeLater(() -> {
      fileTextArea.setEditable(false);
      progressBar.setIndeterminate(true);
    });
  }

  @Override
  public void setCurrentLineNumber(int lineNumber) {
    SwingUtilities.invokeLater(() -> currentLineNumberLabel.setText(String.valueOf(lineNumber)));
  }

  private void onOpenButtonPressed() {
    int result = fileChooser.showOpenDialog(mainFrame);
    if (result == JFileChooser.APPROVE_OPTION) {
      pathToFile = fileChooser.getSelectedFile().toPath();
      fileField.setText(pathToFile.toString());
      presenter.onOpenFile(pathToFile);
    }
  }

  @Override
  public void setScrollPosition(int position) {
    SwingUtilities.invokeLater(() -> scrollBar.setValue(position));
  }

  @Override
  public int getCurrentScrollPosition() {
    return scrollBar.getValue();
  }
}