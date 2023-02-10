package com.wolox.training.services;

import com.wolox.training.dtos.BookDto;

public interface IOpenLibraryService {

    BookDto bookInfo(String isbn);
}
