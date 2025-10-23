package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.Random;

@Service
public class CardGenerator {
    private Random random = new Random();

    public String gerarNumero() {
        StringBuilder numero = new StringBuilder();
        
        for (int i = 0; i < 12; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }

    public int gerarCVV() {
        return 100 + random.nextInt(900);
    }

    public YearMonth gerarValidade() {
        int mes = 1 + random.nextInt(12);
        int ano = YearMonth.now().getYear() + random.nextInt(6);
        return YearMonth.of(ano, mes);
    }

    public String gerarBandeira() {
        String[] bandeiras = {"Visa", "MasterCard", "Elo", "Amex"};
        return bandeiras[random.nextInt(bandeiras.length)];
    }

    public Card gerarCard(String nomeTitular) {
        Card card = new Card();
        card.setNumero(gerarNumero());
        card.setCvv(gerarCVV());
        card.setValidade(gerarValidade());
        card.setBandeira(gerarBandeira());
        card.setNomeTitular(nomeTitular);
        card.setTeste(true);
        return card;
    }
}


