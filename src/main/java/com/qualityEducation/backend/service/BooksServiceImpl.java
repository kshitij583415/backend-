package com.qualityEducation.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qualityEducation.backend.entity.BooksEntity;
import com.qualityEducation.backend.model.Books;
import com.qualityEducation.backend.repository.BooksRepo;

@Service
public class BooksServiceImpl implements BooksService {

    private BooksRepo bookRepo;

    @Autowired
    public BooksServiceImpl(BooksRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public boolean saveBook(Books book, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            System.err.println("File is empty");
            return false;
        }
        try {
            String uploadDirectory = System.getProperty("user.dir") +
                    "/src/main/resources/static/images/books/";
            System.err.println("Upload Directory: " + uploadDirectory);
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            Files.copy(file.getInputStream(), filePath);
            book.setImage(fileName);
            BooksEntity booksEntity = new BooksEntity();
            BeanUtils.copyProperties(book, booksEntity);
            booksEntity.setImage(fileName);
            bookRepo.save(booksEntity);
            System.out.println("Book saved successfully.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Books> getAllBooks() {
        try {
            List<BooksEntity> booksEntity = bookRepo.findAll();
            List<Books> books = new ArrayList<>();
            for (BooksEntity book : booksEntity) {
                Books b = new Books();
                // BeanUtils.copyProperties(book, b);
                b.setId(book.getId());
                b.setTitle(book.getTitle());
                b.setRating(book.getRating());
                b.setImage(book.getImage());
                books.add(b);
            }
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Books BookById(Long id) {
        try {
            BooksEntity booksEntity = bookRepo.findById(id).get();
            Books books = new Books();
            BeanUtils.copyProperties(booksEntity, books);
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
