package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

@Service
public class CardGenerator {
    private Random random = new Random();
    
    // Prefixos válidos por bandeira (IIN - Issuer Identification Number)
    private static final Map<String, String[]> PREFIXOS_BANDEIRA = new HashMap<>();
    
    static {
        PREFIXOS_BANDEIRA.put("Visa", new String[]{"4"});
        PREFIXOS_BANDEIRA.put("MasterCard", new String[]{"51", "52", "53", "54", "55", "2221", "2720"});
        PREFIXOS_BANDEIRA.put("American Express", new String[]{"34", "37"});
        PREFIXOS_BANDEIRA.put("Elo", new String[]{"4011", "4312", "4389", "4514", "4573", "5067", "5090", "6277", "6362", "6363"});
        PREFIXOS_BANDEIRA.put("Hipercard", new String[]{"6062"});
    }

    public String gerarNumero() {
        String bandeira = gerarBandeira();
        return gerarNumeroParaBandeira(bandeira);
    }
    
    public String gerarNumeroParaBandeira(String bandeira) {
        String[] prefixos = PREFIXOS_BANDEIRA.get(bandeira);
        if (prefixos == null) {
            // Fallback para Visa se bandeira não encontrada
            prefixos = PREFIXOS_BANDEIRA.get("Visa");
        }
        
        String prefixo = prefixos[random.nextInt(prefixos.length)];
        int tamanhoTotal = getTamanhoCartao(bandeira);
        
        // Gerar dígitos restantes (exceto o último que é o verificador)
        StringBuilder numero = new StringBuilder(prefixo);
        int digitosRestantes = tamanhoTotal - prefixo.length() - 1;
        
        for (int i = 0; i < digitosRestantes; i++) {
            numero.append(random.nextInt(10));
        }
        
        // Calcular e adicionar dígito verificador
        String base = numero.toString();
        int checkDigit = calcularDigitoLuhn(base);
        
        return base + checkDigit;
    }
    
    private int getTamanhoCartao(String bandeira) {
        switch (bandeira) {
            case "American Express": return 15;
            case "Visa":
            case "MasterCard":
            case "Elo":
            case "Hipercard":
            default: return 16;
        }
    }
    
    private int calcularDigitoLuhn(String numero) {
        int sum = 0;
        boolean alternate = true;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(numero.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit - 9;
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
        String bandeira = gerarBandeira();
        Card card = new Card();
        card.setNumero(gerarNumeroParaBandeira(bandeira));
        card.setCvv(gerarCVV());
        card.setValidade(gerarValidade());
        card.setBandeira(bandeira);
        card.setNomeTitular(nomeTitular);
        card.setTeste(true);
        return card;
    }
}