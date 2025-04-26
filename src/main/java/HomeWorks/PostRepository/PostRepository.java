package HomeWorks.PostRepository;



import HomeWorks.Model.Post;
import HomeWorks.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final AtomicLong nextId = new AtomicLong(1);
    private final ConcurrentMap<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = nextId.getAndIncrement();
            Post newPost = new Post(newId, post.getContent());
            posts.put(newId, newPost);
            return newPost;
        } else {
            return posts.compute(post.getId(), (id, existing) -> {
                if (existing == null) {
                    throw new NotFoundException("Post with id " + id + " not found");
                }
                existing.setContent(post.getContent());
                return existing;
            });
        }
    }

    public void removeById(long id) {
        if (posts.remove(id) == null) {
            throw new NotFoundException("Post with id " + id + " not found");
        }
    }
}