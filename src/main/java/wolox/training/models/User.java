package wolox.training.models;

import com.google.common.base.Preconditions;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.constans.ErrorConstants;

/**
 * Represents struct a user.
 *
 * @author Daniel De La Pava
 */
@Entity
@Table(name = "users")
@ApiModel(description = "User from data base")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ApiModelProperty(notes = "The user username: is the username to login", required = true)
    @NotNull
    private String username;

    @ApiModelProperty(notes = "The user name: is the user's first name", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(notes = "The user birthDate: it's the birthday date", required = true)
    @NotNull
    private LocalDate birthDate;

    /**
     * Represents the foreign association between user and book
     */
    @ApiModelProperty(notes = "The user books: are the books associated with a user", required = false)
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @NotNull
    private List<Book> books = new LinkedList<>();

    public User() {
        // Constructor for JPA
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, String.format(ErrorConstants.NOT_NULL, "username"));
        Preconditions.checkArgument(!username.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "username"));

        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name, String.format(ErrorConstants.NOT_NULL, "name"));
        Preconditions.checkArgument(!name.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "name"));

        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        Preconditions.checkNotNull(birthDate, String.format(ErrorConstants.NOT_NULL, "birthDate"));
        Preconditions.checkArgument(birthDate.isBefore(LocalDate.now()), ErrorConstants.NOT_LATER_CURRENT_DATE);

        this.birthDate = birthDate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        if (books.contains(book)) {
            throw new BookAlreadyOwnedException();
        }
        
        this.books.add(book);
    }

    public void deleteBook(Book book) {
        books.remove(book);
    }
}
