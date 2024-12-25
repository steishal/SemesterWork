package com.servlet;

import com.dao.CategoryDao;
import com.models.Category;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AddCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        CategoryDao categoryDao = (CategoryDao) context.getAttribute("categoryDao");

        try {
            List<Category> categories = categoryDao.getAllCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/WEB-INF/add-category.jsp").forward(req, resp);
        } catch (DbException e) {
            throw new ServletException("Ошибка при загрузке списка категорий", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        CategoryDao categoryDao = (CategoryDao) context.getAttribute("categoryDao");

        String categoryName = req.getParameter("name");
        if (categoryName == null || categoryName.trim().isEmpty()) {
            req.setAttribute("message", "Название категории не может быть пустым.");
            doGet(req, resp);
            return;
        }

        try {
            if (categoryDao.isCategoryExists(categoryName)) {
                req.setAttribute("message", "Категория с таким названием уже существует.");
                doGet(req, resp);
                return;
            }

            Category category = new Category();
            category.setName(categoryName);
            categoryDao.saveCategory(category);
            resp.sendRedirect(req.getContextPath() + "/admin");
        } catch (DbException e) {
            throw new ServletException("Ошибка при добавлении категории", e);
        }
    }
}


