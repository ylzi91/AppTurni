package yurilenzi.AppTurni.entities;

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
public class Utente implements UserDetails {
    @Id
    @Setter(AccessLevel.NONE)
    private String email;
    private String nome, cognome, immagine, documenti;
    @Enumerated(EnumType.STRING)
    private Role role;


    public Utente(String email, String nome, String cognome) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.role = Role.USER;
        this.immagine = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }

    public Utente(String email, String nome, String cognome, Role role) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.role = role;
        this.immagine = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

}
