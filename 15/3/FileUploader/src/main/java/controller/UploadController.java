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
import java.io.InputStream;
import java.io.OutputStream;
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

      try (InputStream inputStream = filePart.getInputStream()) {
        uploadModel.writeFile(filePart.getSubmittedFileName(), inputStream);
      }

      writePathListToResponse(resp);
    } catch (IOException | ServletException e) {
      log.error(e);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    String fileName = req.getParameter("fileName");

    if (fileName != null && uploadModel.contains(fileName)) {
      try (OutputStream outputStream = resp.getOutputStream()) {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-disposition",
            "attachment; filename=" + fileName);

        uploadModel.loadFile(fileName, outputStream);
      } catch (IOException e) {
        log.error(e);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
    } else {
      try {
        writePathListToResponse(resp);
      } catch (IOException e) {
        log.error(e);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
  }

  private void writePathListToResponse(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    resp.setCharacterEncoding("UTF-8");
    List<String> fileNames = uploadModel.getAllFileNames();
    for (String fileName : fileNames) {
      resp.getWriter().write(fileName + "\n");
    }
  }
}
