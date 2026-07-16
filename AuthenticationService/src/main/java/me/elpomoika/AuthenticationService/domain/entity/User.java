package me.elpomoika.AuthenticationService.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.elpomoika.AuthenticationService.domain.enums.Role;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}