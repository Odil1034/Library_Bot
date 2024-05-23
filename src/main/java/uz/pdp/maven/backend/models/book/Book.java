package uz.pdp.maven.backend.models.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.pdp.maven.backend.types.bookTypes.Genre;

import java.io.File;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder

public class Book {
    private String Id;
    private String name;
    private String author;
    private Genre genre;
    private String language;
    private int countOfPage;
    private String description;
    private String photoId;
    private String fileId;
    private Long userId;
    private boolean isComplete;

    public Book() {
        this.Id = UUID.randomUUID().toString();
    }
}
