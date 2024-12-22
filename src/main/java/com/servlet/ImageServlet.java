package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String relativePath = req.getPathInfo();
        if (relativePath == null || relativePath.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image path");
            return;
        }
        String uploadDirPath = "/Users/anastasia/IdeaProjects/SemesterWork2/target/SemesterWork2/uploads";
        Path filePath = Paths.get(uploadDirPath, relativePath);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            return;
        }
        String mimeType = getServletContext().getMimeType(filePath.toString());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        resp.setContentType(mimeType);

        try (OutputStream out = resp.getOutputStream()) {
            Files.copy(filePath, out);
        }
    }
}

