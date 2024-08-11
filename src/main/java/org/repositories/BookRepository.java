package org.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.entities.Book;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {
}
