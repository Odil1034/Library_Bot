package uz.pdp.maven.backend.models.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class Book {

    private Long Id;
    private String name;
    private String author;
    private String description;
    private String photoId;
    private String fileId;
    private Long userId;
    private boolean isCompleted;


}
