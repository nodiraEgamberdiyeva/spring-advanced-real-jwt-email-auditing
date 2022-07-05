package uz.pdp.springadvanced.springjwtwithdbemailauditing.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserLoginDto {
    @NotNull
    @Email   //validate email pattern (example@gmail.com)
    private String email;
    @NotNull
    private String password;
}
