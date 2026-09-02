package com.bookstore.service;

import com.bookstore.exception.NotFoundException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book create(Book book) {
        book.setId(UUID.randomUUID());
        return repository.save(book);
    }

    public Collection<Book> getAll() {
        return repository.findAll();
    }

    public Book getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
    }

    public void delete(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        repository.delete(id);
    }

    public List<Book> search(String query) {
        if (!StringUtils.hasText(query)) {
            throw new IllegalArgumentException("Search query must not be blank");
        }
        return repository.search(query);
    }
}
