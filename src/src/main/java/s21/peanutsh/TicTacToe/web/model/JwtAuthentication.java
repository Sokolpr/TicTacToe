package s21.peanutsh.TicTacToe.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import s21.peanutsh.TicTacToe.domain.model.Role;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class JwtAuthentication implements Authentication {
    private UUID uuid;
    private Set<Role> roles;
    private boolean isAuthenticated;


    @Override
    public Collection<Role> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public UUID getPrincipal() {
        return uuid;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return uuid.toString();
    }
}
