package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.*;

class CardGeneratorTest {

    private CardGenerator cardGenerator;

    @BeforeEach
    void setUp() {
        cardGenerator = new CardGenerator();
    }

    @Test
    void testGerarNumero() {
        String numero = cardGenerator.gerarNumero();
        
        assertNotNull(numero);
        assertEquals(16, numero.length());
        assertTrue(numero.matches("\\d{16}"));
    }

    @Test
    void testGerarCVV() {
        int cvv = cardGenerator.gerarCVV();
        
        assertTrue(cvv >= 100);
        assertTrue(cvv <= 999);
    }

    @Test
    void testGerarValidade() {
        YearMonth validade = cardGenerator.gerarValidade();
        
        assertNotNull(validade);
        assertTrue(validade.compareTo(YearMonth.now()) >= 0);
    }

    @Test
    void testGerarBandeira() {
        String bandeira = cardGenerator.gerarBandeira();
        
        assertNotNull(bandeira);
        assertFalse(bandeira.trim().isEmpty());
        assertTrue(bandeira.equals("Visa") || 
                  bandeira.equals("MasterCard") || 
                  bandeira.equals("Elo") || 
                  bandeira.equals("American Express") || 
                  bandeira.equals("Hipercard"));
    }

    @Test
    void testGerarCard() {
        String nomeTitular = "João Silva";
        Card card = cardGenerator.gerarCard(nomeTitular);
        
        assertNotNull(card);
        assertEquals(nomeTitular, card.getNomeTitular());
        assertNotNull(card.getNumero());
        assertEquals(16, card.getNumero().length());
        assertTrue(card.getCvv() >= 100 && card.getCvv() <= 999);
        assertNotNull(card.getValidade());
        assertNotNull(card.getBandeira());
        assertTrue(card.isTeste());
    }

    @Test
    void testGerarCardComNomeVazio() {
        Card card = cardGenerator.gerarCard("");
        
        assertNotNull(card);
        assertEquals("", card.getNomeTitular());
        assertTrue(card.isTeste());
    }

    @Test
    void testGerarCardComNomeNulo() {
        Card card = cardGenerator.gerarCard(null);
        
        assertNotNull(card);
        assertNull(card.getNomeTitular());
        assertTrue(card.isTeste());
    }
}