package org.repositories;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.entities.Author;
import org.entities.Book;

import java.util.List;

@ApplicationScoped
public class AuthorRepository implements PanacheRepository<Author> {

    public List<Author> findAuthorsPaged(int pageNo, int pageSize){
        PanacheQuery<Author> query = Author.findAll();
        return query.page(pageNo, pageSize).list();
    }
    public List<Author> findAuthorsByIds(List<Long> ids){
        return list("id in ?1", ids);
    }

    public List<Book> findBooksByAuthorId(Long authorId){
        Author author = Author.findById(authorId);
        if(author != null){
            return author.getBooks();
        }
        return null;
    }
}
