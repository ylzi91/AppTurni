package yurilenzi.AppTurni.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"password", "authorities", "role", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class Utente implements UserDetails {
    @Id
    @Setter(AccessLevel.NONE)
    private String email;
    private String nome, cognome, immagine, documenti, password;
    @Enumerated(EnumType.STRING)
    private Role role;


    public Utente(String email, String nome, String cognome, String password) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.role = Role.USER;
        this.immagine = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }

    public Utente(String email, String nome, String cognome, String password, Role role) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.role = role;
        this.immagine = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
