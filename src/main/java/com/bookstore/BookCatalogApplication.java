package com.bookstore;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookCatalogApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(BookService bookService) {
        return args -> {
            Book b1 = new Book();
            b1.setTitle("Clean Code");
            b1.setAuthor("Robert C. Martin");
            b1.setIsbn("978-0132350884");
            bookService.create(b1);

            Book b2 = new Book();
            b2.setTitle("The Pragmatic Programmer");
            b2.setAuthor("Andrew Hunt");
            b2.setIsbn("978-0135957059");
            bookService.create(b2);

            Book b3 = new Book();
            b3.setTitle("Design Patterns");
            b3.setAuthor("Gang of Four");
            b3.setIsbn("978-0201633610");
            bookService.create(b3);
        };
    }
}
