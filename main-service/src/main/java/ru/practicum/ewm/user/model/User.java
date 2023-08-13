package ru.practicum.ewm.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "email", nullable = false)
    @Email(message = "email введен не верно")
    @NotBlank
    @Size(min = 6, max = 254)
    String email;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Имя не может быть пустым или содержать только пробелы")
    @Size(min = 2, max = 250)
    String name;
}