package uz.pdp.maven.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor

public abstract class BaseModel {

    private String Id;

    public BaseModel() {
        Id = UUID.randomUUID().toString();
    }
}
