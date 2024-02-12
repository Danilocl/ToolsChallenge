package com.br.tools.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Transacao {

	private String cartao;

	private String id;

	private Descricao descricao;

	private FormaPagamento formaPagamento;

	public String getCartao() {
		return cartao;
	}

	public void setCartao(String cartao) {
		this.cartao = cartao;
	}

	public Descricao getDescricao() {
		return descricao;
	}

	public void setDescricao(Descricao descricao) {
		this.descricao = descricao;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
