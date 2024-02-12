package com.br.tools.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PagamentosUtil {

	public static String gerarNSU() {
		Random random = new Random();

		StringBuilder nsu = new StringBuilder();

		for (int i = 0; i < 10; i++) {
			nsu.append(random.nextInt(10));
		}

		return nsu.toString();
	}

	public static String gerarCodigoAutorizacao() {
		Random random = new Random();
		StringBuilder codigoAutorizacao = new StringBuilder();

		for (int i = 0; i < 9; i++) {
			codigoAutorizacao.append(random.nextInt(10));
		}

		return codigoAutorizacao.toString();
	}

	public static boolean passaram48HorasDesde(String dataString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime data = LocalDateTime.parse(dataString, formatter);
		LocalDateTime dataMais48Horas = data.plusHours(48);
		LocalDateTime dataAtual = LocalDateTime.now();

		return dataAtual.isAfter(dataMais48Horas);
	}

	public static boolean validaTipo(String tipo) {
		if (!tipo.equalsIgnoreCase("AVISTA") && !tipo.equalsIgnoreCase("PARCELADO LOJA")
				&& !tipo.equalsIgnoreCase("PARCELADO EMISSOR")) {
			return true;
		} else {
			return false;
		}
	}

	public static String criptografaCartao(String cartao) {

		if (cartao == null || cartao.isEmpty()) {
			return null;
		}

		int tamanho = cartao.length();
		String primeiroQuatroDigitos = cartao.substring(0, 4);
		String ultimosQuatroDigitos = cartao.substring(tamanho - 4, tamanho);
		String cartaoCriptografado = primeiroQuatroDigitos + "********" + ultimosQuatroDigitos;

		return cartaoCriptografado;
	}

	public static boolean verificarTamanho(String numero) {
		numero = numero.trim();
		if (!numero.matches("\\d+")) {
			return false;
		}

		return numero.length() == 13 || numero.length() == 16;
	}

}
