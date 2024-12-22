package com.servlet;

import com.dao.PostDao;
import com.models.Post;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deletePost")
public class DeletePostServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");

        if (postDao == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PostDao не инициализирован.");
            return;
        }

        HttpSession session = req.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Необходима авторизация.");
            return;
        }

        try {
            int postId = Integer.parseInt(req.getParameter("postId"));
            System.out.println(postId);
            Post post = postDao.getPostById(postId);
            System.out.println(post);

            if (post != null && post.getUserId().equals(userId)) {
                // Получаем путь к папке загрузок из контекста
                String uploadDirPath = context.getRealPath("/uploads");
                System.out.println(uploadDirPath);
                postDao.deletePost(postId, uploadDirPath);
                resp.sendRedirect(req.getContextPath() + "/mypost");
            } else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Вы не можете удалить этот пост.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный идентификатор поста.");
        } catch (DbException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при удалении поста.");
        }
    }
}


