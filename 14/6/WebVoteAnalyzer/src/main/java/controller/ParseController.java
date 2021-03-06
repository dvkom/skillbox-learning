package controller;

import model.VoteAnalyzer;

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

@WebServlet("/parse")
@MultipartConfig
public class ParseController extends HttpServlet {

    @Inject
    private VoteAnalyzer analyzer;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    Part filePart = req.getPart("file");
    InputStream filePartInputStream = filePart.getInputStream();
    req.setAttribute("voteStationsSchedule", analyzer.analyze(filePartInputStream));

    req.getRequestDispatcher("view/result.jsp").forward(req, resp);
    }
}
