package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String email;
    private String login;
    private String senha;
    private LocalDateTime dataCriacao;
    private LocalDateTime ultimoLogin;
    
    // Associação: Um usuário pode ter muitos cartões
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Card> cartoes;
    
    public User() {
        this.dataCriacao = LocalDateTime.now();
    }
}