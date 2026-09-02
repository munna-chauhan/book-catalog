package com.bookstore.service;

import com.bookstore.exception.NotFoundException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
}
