package uz.pdp.maven.backend.models.myUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class MyUser {

    private Long Id;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String phoneNumber;
    private String baseState;
    private String state;

}
