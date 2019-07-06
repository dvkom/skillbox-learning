package controller;

import model.UploadModel;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@WebServlet("/upload")
@MultipartConfig
public class UploadController extends HttpServlet {
  private static final Logger log = Logger.getLogger(UploadController.class);

  @Inject
  private UploadModel uploadModel;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    try {
      Part filePart = req.getPart("file");
      uploadModel.writeFile(filePart, req.getParameter("path"));
      writePathListToResponse(resp);
    } catch (IOException | ServletException e) {
      log.error(e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    String fileName = req.getParameter("fileName");
    if (fileName != null && uploadModel.contains(Path.of(fileName))) {
      try {
        uploadModel.loadFile(Path.of(fileName), resp);
      } catch (IOException e) {
        log.error(e);
      }
    } else {
      try {
        writePathListToResponse(resp);
      } catch (IOException e) {
        log.error(e);
      }
    }
  }

  private void writePathListToResponse(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    resp.setCharacterEncoding("UTF-8");
    List<Path> paths = uploadModel.getAllPaths();
    for (Path path : paths) {
      resp.getWriter().write(path.toString() + "\n");
    }
  }
}
