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
        return cardGenerator.gerarCard(nomeTitular);
    }
    
    @Override
    public boolean validarCard(Card card) {
        return CardValidator.validar(card);
    }
}