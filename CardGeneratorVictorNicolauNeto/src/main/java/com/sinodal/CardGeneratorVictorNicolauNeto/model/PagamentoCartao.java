package com.sinodal.CardGeneratorVictorNicolauNeto.model;

// Implementação concreta - polimorfismo
public class PagamentoCartao extends Pagamento {
    private Card cartao;
    
    public PagamentoCartao(double valor, String descricao, Card cartao) {
        super(valor, descricao);
        this.cartao = cartao;
    }
    
    @Override
    public boolean processar() {
        // Implementação específica para pagamento com cartão
        return CardValidator.validar(cartao) && valor > 0;
    }
    
    
    public Card getCartao() { return cartao; }
}