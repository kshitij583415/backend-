package com.qualityEducation.backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.model.Books;

public interface BooksService {
    public boolean saveBook(Books book, MultipartFile file);

    public List<Books> getAllBooks();

    public Books BookById(Long id);
}
