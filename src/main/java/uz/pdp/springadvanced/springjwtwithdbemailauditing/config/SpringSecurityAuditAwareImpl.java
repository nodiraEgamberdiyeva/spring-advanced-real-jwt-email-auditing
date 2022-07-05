package uz.pdp.springadvanced.springjwtwithdbemailauditing.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.User;

import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAuditAwareImpl implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()&&
                authentication!=null&&
                !authentication.getPrincipal().equals("anonymousUser")){
            User user = (User) authentication.getPrincipal();
            return Optional.of(user.getID());
        }
        return Optional.empty();
    }
}
