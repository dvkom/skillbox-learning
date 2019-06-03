import javax.swing.*;

public class Loader {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ViewerContract.View view = new ViewerView();
      Model model = new ViewerModel();
      ViewerContract.Presenter presenter = new ViewerPresenter(model, view);
      view.setPresenter(presenter);
    });
  }
}
