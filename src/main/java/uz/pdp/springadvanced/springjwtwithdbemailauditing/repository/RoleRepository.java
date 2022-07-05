package uz.pdp.springadvanced.springjwtwithdbemailauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.Role;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName name);
}
