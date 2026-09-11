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

import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    BookService bookService;

    @Test
    void getBooksData_returnsMappedBooks() throws Exception {

        // --- Мокаем SELECT * FROM books ---
        when(jdbcTemplate.query(eq("SELECT * FROM books"), any(RowMapper.class)))
                .thenAnswer(invocation -> {
                    RowMapper<Book> mapper = invocation.getArgument(1);

                    ResultSet rs = mock(ResultSet.class);
                    when(rs.getInt("id")).thenReturn(1);
                    when(rs.getInt("author_id")).thenReturn(1);
                    when(rs.getString("title")).thenReturn("Idiot");
                    when(rs.getInt("price_old")).thenReturn(350);
                    when(rs.getInt("price")).thenReturn(400);

                    return List.of(mapper.mapRow(rs, 0));
                });

        // --- Мокаем SELECT * FROM authors WHERE id = 1 ---
        when(jdbcTemplate.query(startsWith("select * from authors"), any(RowMapper.class)))
                .thenAnswer(invocation -> {
                    RowMapper<Author> mapper = invocation.getArgument(1);

                    ResultSet rs = mock(ResultSet.class);
                    when(rs.getInt("id")).thenReturn(1);
                    when(rs.getString("first_name")).thenReturn("Fyodor");
                    when(rs.getString("last_name")).thenReturn("Dostoevsky");

                    return List.of(mapper.mapRow(rs, 0));
                });

        // --- Вызываем сервис ---
        List<Book> books = bookService.getBooksData();

        // --- Проверяем ---
        assertEquals(1, books.size());
        assertEquals("Idiot", books.get(0).getTitle());
        assertEquals("Fyodor Dostoevsky", books.get(0).getAuthor());

        // --- Проверяем вызовы ---
        verify(jdbcTemplate).query(eq("SELECT * FROM books"), any(RowMapper.class));
        verify(jdbcTemplate).query(startsWith("select * from authors"), any(RowMapper.class));
    }
}

