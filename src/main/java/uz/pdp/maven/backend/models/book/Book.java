package uz.pdp.maven.backend.models.book;

import com.pengrad.telegrambot.model.PhotoSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.baseModel.BaseModel;

import java.io.File;
import java.nio.file.Path;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder

public class Book extends BaseModel {

    private String name;
    private String author;
    private String description;
    private File file;
    private PhotoSize photo;
    private String coverPictureId;
    private Path path;

}
