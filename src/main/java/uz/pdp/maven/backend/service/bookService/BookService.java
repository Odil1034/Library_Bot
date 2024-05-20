package uz.pdp.maven.backend.service.bookService;

import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.models.myUser.MyUser;
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
        List<Book> books = bookWriterAndLoader.load(Book.class);

        for (int i = 0; i < books.size(); i++) {
            Book curBook = books.get(i);
            if(Objects.equals(book.getFileId(), curBook.getFileId())){
                books.set(i, book);
                bookWriterAndLoader.write(books);
                return;
            }
        }

        books.add(book);
        bookWriterAndLoader.write(books);
    }

    public Book load(String fileId){
        List<Book> books = bookWriterAndLoader.load(Book.class);

        for (int i = 0; i < books.size(); i++) {
            Book curBook = books.get(i);
            if (curBook.getFileId().equals(fileId)) {
                return curBook;
            }
        }

        return null;
    }
}
