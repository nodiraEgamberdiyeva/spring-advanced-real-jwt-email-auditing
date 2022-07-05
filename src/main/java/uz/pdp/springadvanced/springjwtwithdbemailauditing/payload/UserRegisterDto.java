package uz.pdp.springadvanced.springjwtwithdbemailauditing.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRegisterDto {

    @NotNull
    @Size(min = 3, max = 50)  //validate field, from javax.validation, not related to spring jpa
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Email   //validate email pattern (example@gmail.com)
    private String email;
    @NotNull
    private String password;
}
