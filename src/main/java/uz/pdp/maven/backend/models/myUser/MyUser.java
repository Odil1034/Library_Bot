package uz.pdp.maven.backend.models.myUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.baseModel.BaseModel;
import uz.pdp.maven.backend.states.BaseState;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder

public class MyUser extends BaseModel {

    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String phoneNumber;
    private String baseState;
    private String state;

}
