package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.YearMonth;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @Column(length = 64)
    private String numero;

    private String nomeTitular;

    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth validade;

    private int cvv;

    private String bandeira;

    private boolean teste = false;
}
