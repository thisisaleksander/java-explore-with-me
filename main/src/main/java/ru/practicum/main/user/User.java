package ru.practicum.main.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "users", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    @Column(name = "email", nullable = false)
    String email;

    @NotBlank
    @Size(min = 2, max = 250)
    @Column(name = "name", nullable = false)
    String name;
}