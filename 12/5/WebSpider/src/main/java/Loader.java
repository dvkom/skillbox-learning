import contracts.Model;
import models.SearchEngineModel;
import org.apache.log4j.Logger;
import presenters.Presenter;
import views.SearchUrlsView;

import javax.swing.*;

public class Loader {
  private static final Logger log = Logger.getLogger(Loader.class);

  public static void main(String[] args) {
    log.info("The Web Spider was started");
    SwingUtilities.invokeLater(() -> {
      SearchUrlsView form = new SearchUrlsView();
      Model model = new SearchEngineModel();
      Presenter presenter = new Presenter(model, form);
      form.setPresenter(presenter);
    });
  }
}
