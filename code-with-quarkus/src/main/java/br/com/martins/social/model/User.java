package br.com.martins.social.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
}
