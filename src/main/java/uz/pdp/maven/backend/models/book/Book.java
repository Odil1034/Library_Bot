package uz.pdp.maven.backend.models.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.pdp.maven.backend.types.bookTypes.Genre;
import uz.pdp.maven.forExample.SendFile;

import java.util.Objects;

@Data
@AllArgsConstructor
@Builder

public class Book {

    private String name;
    private String author;
    private Genre genre;
    private String description;
    private String photoId;
    private String fileId;
    private Long userId;
    private boolean isComplete;

}
