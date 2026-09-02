package com.bookstore.controller;

import com.bookstore.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postBooks_validBody_returns201WithId() throws Exception {
        Book book = new Book();
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setIsbn("978-1234567890");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void getBooks_returnsAtLeastThreeSeededBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void getBookById_knownId_returns200WithBook() throws Exception {
        Book book = new Book();
        book.setTitle("Fetch Me");
        book.setAuthor("Author One");
        book.setIsbn("978-0000000001");

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Book created = objectMapper.readValue(responseBody, Book.class);

        mockMvc.perform(get("/books/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Fetch Me"));
    }

    @Test
    void deleteBookById_knownId_returns204EmptyBody() throws Exception {
        Book book = new Book();
        book.setTitle("Delete Me");
        book.setAuthor("Author Two");
        book.setIsbn("978-0000000002");

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andReturn();

        Book created = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);

        mockMvc.perform(delete("/books/" + created.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void getBookById_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/books/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void postBooks_blankTitle_returns400() throws Exception {
        Book book = new Book();
        book.setTitle("");
        book.setAuthor("Author");
        book.setIsbn("978-0000000003");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchBooks_validQuery_returns200WithMatchingBook() throws Exception {
        Book book = new Book();
        book.setTitle("Search Test Title");
        book.setAuthor("MatchableUniqueAuthor111");
        book.setIsbn("978-0000000010");

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andReturn();

        Book created = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);

        mockMvc.perform(get("/books/search").param("q", "matchableuniqueauthor111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(created.getId().toString()))
                .andExpect(jsonPath("$[0].author").value("MatchableUniqueAuthor111"));
    }

    @Test
    void searchBooks_noMatch_returns200WithEmptyArray() throws Exception {
        mockMvc.perform(get("/books/search").param("q", "zzznomatchzzz999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void searchBooks_missingQ_returns400() throws Exception {
        mockMvc.perform(get("/books/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchBooks_blankQ_returns400() throws Exception {
        mockMvc.perform(get("/books/search").param("q", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchBooks_caseInsensitive_returns200WithMatch() throws Exception {
        Book book = new Book();
        book.setTitle("CaseInsensitiveTitleUnique777");
        book.setAuthor("Some Author");
        book.setIsbn("978-0000000011");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/books/search").param("q", "CASEINSENSITIVETITLEUNIQUE777"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].title").value("CaseInsensitiveTitleUnique777"));
    }
}
