package uz.pdp.springadvanced.springjwtwithdbemailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {  //to use user as userDetail class, should implement all abstract methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }



    @Id
    @GeneratedValue
    private UUID ID;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String emailCode;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)  //to restrict update action
    @CreationTimestamp //set created timestamp automatically
    private Timestamp createdAt;

    @UpdateTimestamp  //set updated timestamp automatically
    private Timestamp updateAt;

    //there are two types of fetch: lazy and eager
    //if fetch type is eager when we call user it always takes the role with it
    //if fetch type is lazy when we call user it doesn't call role unless we use call
    @ManyToMany //(fetch = FetchType.EAGER) we can replace it with adding property to app.prop //to avoid lazy init exception
    private List<Role> roles;

    //we can manage user status with this fields
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled;
}
