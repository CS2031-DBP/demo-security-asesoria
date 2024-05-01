package org.demo.security.user;

import jakarta.persistence.*;
import lombok.Data;
import org.demo.security.item.Item;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Role role;

    String firstName;
    String lastName;
    Integer age;
    String email;
    String password;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    Set<Item> items;

    @Transient
    String role_prefix = "ROLE_";


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role_prefix + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
