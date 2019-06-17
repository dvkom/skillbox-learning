package controller;

import model.VoteAnalyzer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/parse")
@MultipartConfig
public class ParseController extends HttpServlet {
  private VoteAnalyzer analyzer = new VoteAnalyzer();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    Part filePart = req.getPart("file");
    InputStream filePartInputStream = filePart.getInputStream();
    if (filePartInputStream.available() != 0) {
      analyzer.analyze(filePartInputStream);
      req.setAttribute("voteStationsSchedule", analyzer.getVoteStationsSchedule());
      req.setAttribute("workDays", analyzer.getWorkDays());
    }
    req.getRequestDispatcher("view/result.jsp").forward(req, resp);
  }
}
