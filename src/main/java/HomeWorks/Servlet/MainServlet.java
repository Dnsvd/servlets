package HomeWorks.Servlet;

import HomeWorks.PostController.PostController;
import HomeWorks.PostRepository.PostRepository;
import HomeWorks.Service.PostService;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/*")
//public class MainServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
//        resp.setContentType("text/plain");
//        try {
//            resp.getWriter().println("Работает! Путь: " + req.getRequestURI());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

public class MainServlet extends HttpServlet {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String PATH = "/api/posts";

    private PostController controller;

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        res.getWriter().print("Hello World");
    }

        @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final var path = req.getServletPath();
            final var method = req.getMethod();

            if ("/".equals(path) && "GET".equals(method)) {
                resp.sendRedirect("/api/posts");
                return;
            }

            if (method.equals(GET) && path.equals(PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(PATH + "/\\d+")) {
                final var id = extractId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(PATH + "/\\d+")) {
                final var id = extractId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long extractId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")).replace("/",""));
    }
}