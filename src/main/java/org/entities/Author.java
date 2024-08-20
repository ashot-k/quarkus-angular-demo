package org.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "authors")
public class Author extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;
    @Column(nullable = false)
    @NotEmpty(message = "Author name must be specified and not be empty")
    private String name;
    @Column(nullable = false)
    @NotNull(message = "Author age must be specified")
    private Short age;
    @Version
    private Long version;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "author_book"
            , joinColumns = @JoinColumn(name = "author_id")
            , inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @JsonIgnore
    private List<Book> books;

    public Author() {
    }

    public Author(String name, short age) {
        this.age = age;
        this.name = name;
    }

    public Author(String name, short age, List<Book> books) {
        this.name = name;
        this.age = age;
        this.books = books;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
