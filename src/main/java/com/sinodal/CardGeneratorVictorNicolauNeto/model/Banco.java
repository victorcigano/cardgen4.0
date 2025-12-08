package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Banco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String codigo;
    
    // Agregação: Banco tem cartões, mas cartões podem existir sem banco
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "banco_id")
    private List<Card> cartoes;
}