package views;

import contracts.SearchUrlsContract;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.text.NumberFormat;

public class SearchUrlsView implements SearchUrlsContract.View {
  private JFrame mainFrame;
  private JPanel rootPanel;
  private JLabel urlLabel;
  private JTextField urlField;
  private JLabel saveLabel;
  private JTextField saveField;
  private JButton choiceButton;
  private JLabel pauseLabel;
  private JFormattedTextField pauseField;
  private JLabel millisLabel;
  private JLabel processedPagesLabel;
  private JLabel processedPagesCountLabel;
  private JButton startButton;
  private JLabel pagesInQueueLabel;
  private JLabel pagesInQueueCountLabel;
  private JButton pauseButton;
  private JLabel stopwatchLabel;
  private JLabel secondsLabel;
  private JButton stopButton;
  private JProgressBar progressBar;
  private JFileChooser fileChooser;

  private static final Logger log = Logger.getLogger(SearchUrlsView.class);
  private Path pathToSave = Path.of("D:\\site_URLs.txt");
  private SearchUrlsContract.Presenter searchPresenter;

  private static final Color VALID_COLOR = new Color(24, 156, 1);
  private static final Color INVALID_COLOR = new Color(177, 3, 0);

  public SearchUrlsView() {
    mainFrame = new JFrame();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      log.error("Error setting a look and feel ", e);
    }
    mainFrame.setTitle("Web Spider");
    mainFrame.setSize(540, 220);
    mainFrame.setResizable(false);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mainFrame.setVisible(true);
    mainFrame.setContentPane(rootPanel);

    urlField.setBackground(VALID_COLOR);
    initFileChooser();
    log.debug("Frame was initialized");
  }

  @Override
  public void setPresenter(SearchUrlsContract.Presenter presenter) {
    this.searchPresenter = presenter;
    setListeners();
  }

  private void setListeners() {
    urlField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        searchPresenter.onUrlTyped();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        searchPresenter.onUrlTyped();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        searchPresenter.onUrlTyped();
      }
    });

    choiceButton.addActionListener(e -> onChoiceButtonPressed());
    startButton.addActionListener(e -> searchPresenter.onStartSearch());
    stopButton.addActionListener(e -> searchPresenter.onStopSearch());
    pauseButton.addActionListener(e -> searchPresenter.onPauseSearch());
    log.debug("Listeners were set");
  }

  private void createUIComponents() {
    NumberFormat format = NumberFormat.getIntegerInstance();
    NumberFormatter formatter = new NumberFormatter(format);
    formatter.setValueClass(Integer.class);
    formatter.setMinimum(0);
    formatter.setMaximum(Integer.MAX_VALUE);
    pauseField = new JFormattedTextField(formatter);
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
    fileChooser.setDialogTitle("Сохранение файла");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setSelectedFile(new File("site_URLs.txt"));
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Текстовые документы (*.txt)", "txt");
    fileChooser.setFileFilter(filter);
  }

  private void onChoiceButtonPressed() {
    int result = fileChooser.showSaveDialog(mainFrame);
    if (result == JFileChooser.APPROVE_OPTION) {
      pathToSave = fileChooser.getSelectedFile().toPath();
      saveField.setText(pathToSave.toString());
    }
  }

  @Override
  public String getStartUrl() {
    return urlField.getText();
  }

  @Override
  public String getPauseValue() {
    return pauseField.getText();
  }

  @Override
  public Path getPathToSaveResult() {
    return pathToSave;
  }

  @Override
  public void showStartedView() {
    startButton.setEnabled(false);
    stopButton.setEnabled(true);
    pauseButton.setEnabled(true);
    urlField.setEditable(false);
    pauseField.setEditable(false);
    choiceButton.setEnabled(false);
    progressBar.setIndeterminate(true);
  }

  @Override
  public void showStoppedView() {
    stopButton.setEnabled(false);
    startButton.setEnabled(true);
    pauseButton.setEnabled(false);
    pauseButton.setText("Пауза");
    urlField.setEditable(true);
    pauseField.setEditable(true);
    choiceButton.setEnabled(true);
  }

  @Override
  public void showSuspendState() {
    pauseButton.setText("Продолжить");
  }

  @Override
  public void showResumeState() {
    pauseButton.setText("Пауза");
  }

  @Override
  public void showStatistic(String processedUrls, String unprocessedUrls) {
    processedPagesCountLabel.setText(processedUrls);
    pagesInQueueCountLabel.setText(unprocessedUrls);
  }

  @Override
  public void showStopwatch(String time) {
    secondsLabel.setText(time);
  }

  @Override
  public void showReadyView() {
    urlField.setBackground(VALID_COLOR);
    startButton.setEnabled(true);
  }

  @Override
  public void showNotReadyView() {
    urlField.setBackground(INVALID_COLOR);
    startButton.setEnabled(false);
  }

  @Override
  public void showProgress() {
    progressBar.setIndeterminate(true);
  }

  @Override
  public void hideProgress() {
    progressBar.setIndeterminate(false);
  }

  @Override
  public void showFileWritingErrorMessage() {
    JOptionPane.showConfirmDialog(rootPanel,
        "В процессе записи файла произошла ошибка. Попробуйте снова.",
        "Ошибка", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void showFileWritingSuccessMessage() {
    JOptionPane.showConfirmDialog(rootPanel,
        "Результат успешно записан в файл: \n" + pathToSave.toString(),
        "Информация", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
  }
}
