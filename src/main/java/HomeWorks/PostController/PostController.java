package HomeWorks.PostController;

import HomeWorks.Service.PostService;
import HomeWorks.exception.NotFoundException;
import com.google.gson.Gson;
import HomeWorks.Model.Post;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;
    private final Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().print(gson.toJson(service.all()));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        try {
            response.setContentType(APPLICATION_JSON);
            response.getWriter().print(gson.toJson(service.getById(id)));
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        try {
            response.setContentType(APPLICATION_JSON);
            Post post = gson.fromJson(body, Post.class);
            Post saved = service.save(post);
            response.getWriter().print(gson.toJson(saved));
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        try {
            service.removeById(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}