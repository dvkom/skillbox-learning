import javax.swing.*;
import java.awt.*;

public class StopwatchFrame extends JFrame {
  private JPanel rootPanel;
  private JLabel secondsLabel;
  private JButton startAndStopButton;
  private JButton pauseAndResumeButton;
  private boolean isStarted = false;
  private boolean isPaused = false;
  private static final int WIDTH = 400;
  private static final int HEIGHT = 300;
  private Thread updater;
//  private SynchronizedStopwatch stopwatch = new SynchronizedStopwatch();
  private AtomicStopwatch stopwatch = new AtomicStopwatch();

  public StopwatchFrame() throws HeadlessException {
    init();

    startAndStopButton.addActionListener(e -> {
      if (!isStarted) {
        setState(StopwatchState.STARTED);
        updater = new Thread(() -> {
          while (true) {
            long elapsedTime = stopwatch.getElapsedTime();
            SwingUtilities.invokeLater(() ->
                secondsLabel.setText(String.format("%d:%02d:%02d",
                    elapsedTime / 6_000, elapsedTime / 100 % 60, elapsedTime % 100))
            );
            try {
              Thread.sleep(10);
            } catch (InterruptedException e1) {
              break;
            }
          }
        });
        updater.start();
      } else {
        updater.interrupt();
        setState(StopwatchState.STOPPED);
      }
    });

    pauseAndResumeButton.addActionListener(e -> {
      if (!isPaused) {
        setState(StopwatchState.PAUSED);
      } else {
        setState(StopwatchState.RESUMED);
      }
    });
  }

  private void setState(StopwatchState state) {
    switch (state) {
      case STARTED:
        isStarted = true;
        stopwatch.start();
        startAndStopButton.setText("Stop!");
        pauseAndResumeButton.setEnabled(true);
        break;
      case STOPPED:
        isStarted = false;
        isPaused = false;
        startAndStopButton.setText("Start!");
        pauseAndResumeButton.setText("Pause");
        pauseAndResumeButton.setEnabled(false);
        break;
      case PAUSED:
        isPaused = true;
        stopwatch.pause();
        pauseAndResumeButton.setText("Resume");
        break;
      case RESUMED:
        isPaused = false;
        stopwatch.resume();
        pauseAndResumeButton.setText("Pause");
        break;
    }
  }

  private void init() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    setSize(WIDTH, HEIGHT);
    setTitle("Stopwatch");
    setResizable(false);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setVisible(true);
    createAndPackComponents();
  }

  private JPanel createRootPanel() {
    rootPanel = new JPanel();
    rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
    rootPanel.setSize(new Dimension(WIDTH, HEIGHT));
    return rootPanel;
  }

  private JLabel createSecondsLabel() {
    secondsLabel = new JLabel("0:00:00");
    secondsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    secondsLabel.setMinimumSize(new Dimension(160, 80));
    secondsLabel.setPreferredSize(new Dimension(160, 80));
    secondsLabel.setMaximumSize(new Dimension(160, 80));
    secondsLabel.setFont(new Font("Tahoma", Font.PLAIN, 42));
    return secondsLabel;
  }

  private JButton createStartButton() {
    startAndStopButton = new JButton("Start!");
    startAndStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    startAndStopButton.setMinimumSize(new Dimension(WIDTH - 100, 40));
    startAndStopButton.setPreferredSize(new Dimension(WIDTH - 100, 40));
    startAndStopButton.setMaximumSize(new Dimension(WIDTH - 100, 40));
    startAndStopButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
    return startAndStopButton;
  }

  private JButton createPauseButton() {
    pauseAndResumeButton = new JButton("Pause!");
    pauseAndResumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    pauseAndResumeButton.setMinimumSize(new Dimension(WIDTH - 100, 40));
    pauseAndResumeButton.setPreferredSize(new Dimension(WIDTH - 100, 40));
    pauseAndResumeButton.setMaximumSize(new Dimension(WIDTH - 100, 40));
    pauseAndResumeButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
    pauseAndResumeButton.setEnabled(false);
    return pauseAndResumeButton;
  }

  private void createAndPackComponents() {
    setContentPane(createRootPanel());
    rootPanel.add(Box.createVerticalStrut(40));
    rootPanel.add(createSecondsLabel());
    rootPanel.add(Box.createVerticalStrut(40));
    rootPanel.add(createStartButton());
    rootPanel.add(Box.createVerticalStrut(10));
    rootPanel.add(createPauseButton());
  }

  private enum StopwatchState {
    STARTED,
    STOPPED,
    PAUSED,
    RESUMED
  }
}