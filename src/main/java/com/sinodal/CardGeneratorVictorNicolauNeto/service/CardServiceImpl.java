package com.sinodal.CardGeneratorVictorNicolauNeto.service;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardGenerator;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardValidator;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {
    
    private final CardGenerator cardGenerator = new CardGenerator();
    
    @Override
    public Card gerarCard(String nomeTitular, String bandeira) {
        Card card = cardGenerator.gerarCard(nomeTitular);
        card.setBandeira(bandeira);
        // Gera número específico para a bandeira
        card.setNumero(cardGenerator.gerarNumeroParaBandeira(bandeira));
        return card;
    }
    
    @Override
    public boolean validarCard(Card card) {
        return CardValidator.validar(card);
    }
}