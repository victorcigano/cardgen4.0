package com.sinodal.CardGeneratorVictorNicolauNeto.service;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;

// Interface com dois métodos
public interface CardService {
    Card gerarCard(String nomeTitular, String bandeira);
    boolean validarCard(Card card);
}