package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import org.junit.jupiter.api.Test;
import java.time.YearMonth;
import static org.junit.jupiter.api.Assertions.*;

class CardValidatorTest {

    @Test
    void testValidarCardNulo() {
        assertFalse(CardValidator.validar(null));
    }

    @Test
    void testValidarCardTeste() {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setNomeTitular("João Silva");
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(true);
        
        assertTrue(CardValidator.validar(card));
    }

    @Test
    void testValidarCardSemNome() {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }

    @Test
    void testValidarCardNomeVazio() {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setNomeTitular("");
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }

    @Test
    void testValidarCardNomeMuitoLongo() {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setNomeTitular("A".repeat(101)); // 101 caracteres
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }

    @Test
    void testValidarCardSemBandeira() {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setNomeTitular("João Silva");
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }

    @Test
    void testValidarCardNumeroInvalido() {
        Card card = new Card();
        card.setNumero("123456789012345"); // 15 dígitos
        card.setNomeTitular("João Silva");
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }

    @Test
    void testValidarCardNumeroComLetras() {
        Card card = new Card();
        card.setNumero("123456789012345A");
        card.setNomeTitular("João Silva");
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }

    @Test
    void testValidarCardCVVInvalido() {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setNomeTitular("João Silva");
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(99); // CVV muito baixo
        card.setBandeira("Visa");
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }

    @Test
    void testValidarCardValidadeExpirada() {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setNomeTitular("João Silva");
        card.setValidade(YearMonth.now().minusMonths(1)); // Expirado
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(false);
        
        assertFalse(CardValidator.validar(card));
    }
}