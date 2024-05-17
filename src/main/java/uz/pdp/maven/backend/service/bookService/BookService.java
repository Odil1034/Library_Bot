package uz.pdp.maven.backend.service.bookService;

import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.service.BaseService;
import uz.pdp.maven.backend.utils.fileWriterAndLoader.FileWriterAndLoader;

import java.util.List;
import java.util.Objects;

import static uz.pdp.maven.backend.paths.PathConstants.BOOKS_JSON;

public class BookService implements BaseService {

    FileWriterAndLoader<Book> bookWriterAndLoader;

    public BookService() {
        this.bookWriterAndLoader = new FileWriterAndLoader<>(BOOKS_JSON);
    }

    public void save(Book book){
        List<Book> books = bookWriterAndLoader.load();

        for (int i = 0; i < books.size(); i++) {
            Book curBook = books.get(i);
            if(Objects.equals(book.getId(), curBook.getId())){
                books.set(i, book);
                bookWriterAndLoader.write(books);
                return;
            }
        }

        books.add(book);
        bookWriterAndLoader.write(books);
        return;
    }

    public Book load(Long Id){
        List<Book> books = bookWriterAndLoader.load();

        for (int i = 0; i < books.size(); i++) {
            Book curBook = books.get(i);
            if (Objects.equals(curBook.getId(), Id)) {
                return curBook;
            }
        }

        return null;
    }
}
