package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookService bookService;

    @Test
    void getBooksData_returnsMappedBooks() {
        // --- Мок ответа SELECT * FROM books ---
        Book book = new Book();
        book.setId(1);
        book.setAuthor("Fyodor Dostoevsky");
        book.setTitle("Idiot");
        book.setPriceOld(350);
        book.setPrice(400);

        when(jdbcTemplate.query(eq("SELECT * FROM books"), any(RowMapper.class)))
                .thenReturn(List.of(book));

        // --- Мок ответа SELECT * FROM authors WHERE id = X ---
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Fyodor");
        author.setLastName("Dostoevsky");

        when(jdbcTemplate.query(startsWith("select * from authors"), any(RowMapper.class)))
                .thenReturn(List.of(author));

        // --- Вызываем сервис ---
        List<Book> result = bookService.getBooksData();

        // --- Проверяем ---
        assertEquals(1, result.size());
        assertEquals("Idiot", result.get(0).getTitle());
        assertEquals("Fyodor Dostoevsky", result.get(0).getAuthor());

        // --- Проверяем вызовы JdbcTemplate ---
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM books"), any(RowMapper.class));
        verify(jdbcTemplate, times(1)).query(startsWith("select * from authors"), any(RowMapper.class));
    }
}
