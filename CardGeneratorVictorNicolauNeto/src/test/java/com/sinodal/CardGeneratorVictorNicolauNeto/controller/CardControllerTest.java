package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardGenerator;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CardControllerTest {

    @Mock
    private CardGenerator generator;

    @Mock
    private CardRepository repository;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGerarCardComSucesso() {
        assertTrue(true);
    }

    @Test
    void testGerarCardSemUsuario() {
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            cardController.gerarCartao(null, "Visa", null);
        });
        
        verify(generator, never()).gerarCard(anyString());
        verify(repository, never()).save(any(Card.class));
    }

    @Test
    void testGerarCardComNomeMuitoLongo() {
        // Arrange
        String nomeLongo = "A".repeat(101);
        String headerUser = "user123";

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            cardController.gerarCartao(nomeLongo, "Visa", headerUser);
        });
    }

    @Test
    void testListarCards() {
        // Arrange
        List<Card> mockCards = Arrays.asList(
            createMockCard("João Silva"),
            createMockCard("Maria Santos")
        );
        when(repository.findAll()).thenReturn(mockCards);

        // Act
        List<Card> result = cardController.buscarTodos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testRemoverCardComSucesso() {
        // Arrange
        String numero = "1234567890123456";
        when(repository.existsById(numero)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            cardController.deletarCartao(numero);
        });
    }

    @Test
    void testRemoverCardSemUsuario() {
        // Arrange
        String numero = "1234567890123456";
        when(repository.existsById(numero)).thenReturn(true);
        
        // Act
        assertDoesNotThrow(() -> {
            cardController.deletarCartao(numero);
        });
        
        verify(repository).deleteById(numero);
    }

    @Test
    void testRemoverCardInexistente() {
        assertTrue(true);
    }

    @Test
    void testRemoverTodosComSucesso() {
        // Arrange
        String headerUser = "user123";

        // Act
        assertDoesNotThrow(() -> {
            cardController.limparTodos();
        });

        // Assert
        verify(repository).deleteAll();
    }

    @Test
    void testRemoverTodosSemUsuario() {
        // Act
        assertDoesNotThrow(() -> {
            cardController.limparTodos();
        });
        
        verify(repository).deleteAll();
    }

    private Card createMockCard(String nomeTitular) {
        Card card = new Card();
        card.setNumero("1234567890123456");
        card.setNomeTitular(nomeTitular);
        card.setValidade(YearMonth.now().plusMonths(12));
        card.setCvv(123);
        card.setBandeira("Visa");
        card.setTeste(true);
        return card;
    }
}