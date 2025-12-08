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
        PREFIXOS_BANDEIRA.put("MasterCard", new String[]{"51", "52", "53", "54", "55", "2221", "2300", "2500", "2700"});
        PREFIXOS_BANDEIRA.put("American Express", new String[]{"34", "37"});
        PREFIXOS_BANDEIRA.put("Elo", new String[]{"4011", "4312", "4389", "4514", "4573", "5067", "5090", "6277", "6362", "6363", "636297", "636368", "438935"});
        PREFIXOS_BANDEIRA.put("Hipercard", new String[]{"6062"});
    }

    public String gerarNumero() {
        return gerarNumeroParaBandeira("Visa");
    }
    
    public String gerarNumeroParaBandeira(String bandeira) {
        String[] prefixos = PREFIXOS_BANDEIRA.get(bandeira);
        if (prefixos == null) {
            // Fallback para Visa se bandeira não encontrada
            prefixos = PREFIXOS_BANDEIRA.get("Visa");
        }
        
        String numeroFinal;
        int tentativas = 0;
        
        do {
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
            numeroFinal = base + checkDigit;
            
            tentativas++;
        } while (!validarLuhn(numeroFinal) && tentativas < 100);
        
        return numeroFinal;
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
        
        // Processa da direita para esquerda, dobrando dígitos alternados
        for (int i = 0; i < numero.length(); i++) {
            int digit = Character.getNumericValue(numero.charAt(numero.length() - 1 - i));
            
            // Dobra dígitos em posições ímpares (contando da direita, começando em 0)
            if (i % 2 == 1) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }
            
            sum += digit;
        }
        
        // Retorna o dígito que torna a soma múltipla de 10
        int checkDigit = (10 - (sum % 10)) % 10;
        return checkDigit;
    }
    
    private boolean validarLuhn(String numero) {
        int sum = 0;
        boolean doubleDigit = false;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(numero.charAt(i));
            
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }
            
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        
        return (sum % 10 == 0);
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