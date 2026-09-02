package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class BookRepository {

    private final ConcurrentHashMap<UUID, Book> store = new ConcurrentHashMap<>();

    public Book save(Book book) {
        store.put(book.getId(), book);
        return book;
    }

    public Collection<Book> findAll() {
        return store.values();
    }

    public Optional<Book> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    public void delete(UUID id) {
        store.remove(id);
    }

    public List<Book> search(String query) {
        String lower = query.toLowerCase();
        return store.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower)
                        || b.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}
