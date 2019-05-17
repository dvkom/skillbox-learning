import javax.swing.*;
import java.awt.*;

public class Stopwatch extends JFrame {
  private JPanel rootPanel;
  private JLabel secondsLabel;
  private JButton startAndStopButton;
  private JButton pauseButton;
  private boolean isStarted = false;
  private int millis = 0;
  private int minutes = 0;
  private static final int WIDTH = 400;
  private static final int HEIGHT = 300;
  private Thread stopwatch;

  public Stopwatch() throws HeadlessException {
    init();

    startAndStopButton.addActionListener(e -> {
      if (!isStarted) {
        isStarted = true;
        stopwatch = new Thread(() -> {
          while (true) {
            millis++;
            if (millis >= 6_000) {
              minutes++;
              millis = 0;
            }
            secondsLabel.setText(String.format(
                "%d:%02d:%02d", minutes, millis / 100 % 100, millis % 100
            ));
            try {
              Thread.sleep(10);
            } catch (InterruptedException e1) {
              break;
            }
          }
        });
        stopwatch.start();
        startAndStopButton.setText("Stop!");
        pauseButton.setEnabled(true);
      } else {
        isStarted = false;
        stopwatch.interrupt();
        millis = 0;
        minutes = 0;
        secondsLabel.setText("0:00:00");
        startAndStopButton.setText("Start!");
        pauseButton.setEnabled(false);
      }
    });

    pauseButton.addActionListener(e -> {
      isStarted = false;
      stopwatch.interrupt();
      startAndStopButton.setText("Start!");
      pauseButton.setEnabled(false);
    });
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
    pauseButton = new JButton("Pause!");
    pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    pauseButton.setMinimumSize(new Dimension(WIDTH - 100, 40));
    pauseButton.setPreferredSize(new Dimension(WIDTH - 100, 40));
    pauseButton.setMaximumSize(new Dimension(WIDTH - 100, 40));
    pauseButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
    pauseButton.setEnabled(false);
    return pauseButton;
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
}