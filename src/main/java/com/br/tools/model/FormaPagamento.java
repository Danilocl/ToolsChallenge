package com.br.tools.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class FormaPagamento {

	private String tipo;

	private String parcelas;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getParcelas() {
		return parcelas;
	}

	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

}
