package uz.pdp.springadvanced.springjwtwithdbemailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role implements GrantedAuthority { //UserDetails.getAuthorities method returns GrantedAuthority, so we should implement it
    @Override
    public String getAuthority() {
        return this.name.name();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)  //by default jpa save enum type as Ordinal(numbers), we set it as String
    private RoleName name;
}
