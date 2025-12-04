package com.sinodal.CardGeneratorVictorNicolauNeto.factory;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardGenerator;

// Padrão Factory
public class CardFactory {
    private static CardFactory instance;
    private final CardGenerator generator = new CardGenerator();
    
    private CardFactory() {}
    
    public static CardFactory getInstance() {
        if (instance == null) {
            instance = new CardFactory();
        }
        return instance;
    }
    
    public Card criarCard(String tipo, String nomeTitular) {
        Card card = generator.gerarCard(nomeTitular);
        if (tipo != null) {
            card.setBandeira(tipo);
            card.setNumero(generator.gerarNumeroParaBandeira(tipo));
        }
        return card;
    }
}