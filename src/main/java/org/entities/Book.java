package org.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


@Entity
@Table(name = "books")
public class Book extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    @Column(nullable = false)
    @NotEmpty(message = "Book title must be specified and not be empty")
    private String title;
    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private List<Author> authors;
    @Column
    private String image;
    @Version
    private Long version;

    public Book() {
    }

    public Book(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public Book(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
