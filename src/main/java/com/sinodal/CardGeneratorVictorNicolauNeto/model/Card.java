package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.YearMonth;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @Column(length = 16)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 digits")
    private String numero;

    @NotBlank(message = "Cardholder name is required")
    @Size(max = 100, message = "Cardholder name must not exceed 100 characters")
    @Column(length = 100)
    private String nomeTitular;

    @NotNull(message = "Expiry date is required")
    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth validade;

    @Min(value = 100, message = "CVV must be at least 100")
    @Max(value = 999, message = "CVV must be at most 999")
    private int cvv;

    @NotBlank(message = "Card brand is required")
    @Size(max = 50, message = "Card brand must not exceed 50 characters")
    @Column(length = 50)
    private String bandeira;

    @Column(nullable = false)
    private boolean teste = false;
    
    // Associação: Card pertence a um usuário
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;
}
