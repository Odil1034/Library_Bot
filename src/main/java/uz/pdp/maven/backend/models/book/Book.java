package uz.pdp.maven.backend.models.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.maven.backend.models.BaseModel;
import uz.pdp.maven.backend.types.bookTypes.Genre;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder

public class Book extends BaseModel {
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
}
