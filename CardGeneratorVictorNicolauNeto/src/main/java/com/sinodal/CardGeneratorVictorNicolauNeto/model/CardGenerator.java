package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.Random;

@Service
public class CardGenerator {
    private Random random = new Random();

    public String gerarNumero() {
        // Gerar 15 dígitos aleatórios
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            numero.append(random.nextInt(10));
        }
        
        // Calcular dígito verificador usando algoritmo de Luhn
        String base = numero.toString();
        int checkDigit = calcularDigitoLuhn(base);
        
        return base + checkDigit;
    }
    
    private int calcularDigitoLuhn(String numero) {
        int sum = 0;
        boolean alternate = true;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(numero.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return (10 - (sum % 10)) % 10;
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
        String[] bandeiras = {"Visa", "MasterCard", "Elo", "American Express", "Hipercard"};
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


