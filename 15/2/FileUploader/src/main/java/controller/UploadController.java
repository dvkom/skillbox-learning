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

@WebServlet("/upload")
@MultipartConfig
public class UploadController extends HttpServlet {
  private static final Logger log = Logger.getLogger(UploadController.class);

  @Inject
  private UploadModel uploadModel;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try {
      Part filePart = req.getPart("file");
      String fileName = filePart.getSubmittedFileName();
      uploadModel.writeFile(filePart, req.getParameter("path"));

      req.setAttribute("fileName", fileName);
    } catch (IOException e) {
      log.error(e);
      req.setAttribute("fileName", "File uploading error");
    }

    req.getRequestDispatcher("view/result.jsp").forward(req, resp);
  }
}
