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
        // Arrange
        String nomeTitular = "João Silva";
        String headerUser = "user123";
        Card mockCard = createMockCard(nomeTitular);
        
        when(generator.gerarCard(nomeTitular)).thenReturn(mockCard);
        when(repository.save(any(Card.class))).thenReturn(mockCard);

        // Act
        Card result = cardController.gerarCard(headerUser, nomeTitular);

        // Assert
        assertNotNull(result);
        assertEquals(nomeTitular, result.getNomeTitular());
        verify(generator).gerarCard(nomeTitular);
        verify(repository).save(mockCard);
    }

    @Test
    void testGerarCardSemUsuario() {
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            cardController.gerarCard(null, null);
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
            cardController.gerarCard(headerUser, nomeLongo);
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
        List<Card> result = cardController.listarCards();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testRemoverCardComSucesso() {
        // Arrange
        String numero = "1234567890123456";
        String headerUser = "user123";
        when(repository.existsById(numero)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> {
            cardController.removerCard(numero, headerUser);
        });

        // Assert
        verify(repository).existsById(numero);
        verify(repository).deleteById(numero);
    }

    @Test
    void testRemoverCardSemUsuario() {
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            cardController.removerCard("1234567890123456", null);
        });
        
        verify(repository, never()).deleteById(anyString());
    }

    @Test
    void testRemoverCardInexistente() {
        // Arrange
        String numero = "1234567890123456";
        String headerUser = "user123";
        when(repository.existsById(numero)).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            cardController.removerCard(numero, headerUser);
        });
        
        verify(repository, never()).deleteById(anyString());
    }

    @Test
    void testRemoverTodosComSucesso() {
        // Arrange
        String headerUser = "user123";

        // Act
        assertDoesNotThrow(() -> {
            cardController.removerTodos(headerUser);
        });

        // Assert
        verify(repository).deleteAll();
    }

    @Test
    void testRemoverTodosSemUsuario() {
        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            cardController.removerTodos(null);
        });
        
        verify(repository, never()).deleteAll();
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