package uz.pdp.maven.backend.models.myUser;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
