package wolox.training.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * This method consults book by author
     *
     * @param bookAuthor
     * @return the book object
     */
    @GetMapping("/author/{bookAuthor}")
    public Book findByAuthor(@PathVariable String bookAuthor) {
        return bookRepository.findByAuthor(bookAuthor).orElseThrow(BookNotFoundException::new);
    }

    /**
     * This method returns a book per id
     *
     * @param id: this is the unique identifier generated by the database
     * @return the book object
     */
    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    /**
     * This method is used to save a book
     *
     * @param book: receives the structure or book model
     * @return the book object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * This method removes a book by its id
     *
     * @param id: this is the unique identifier generated by the database
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    /**
     * This method updates a book but validates that this book exists and also validates the ids
     *
     * @param book: receives the structure or book model
     * @param id: this is the unique identifier generated by the database
     * @return the book object
     */
    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (!book.getId().equals(id)) {
            throw new BookIdMismatchException();
        }

        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

}
