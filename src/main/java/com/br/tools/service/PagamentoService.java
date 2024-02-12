package com.br.tools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.tools.model.Pagamento;
import com.br.tools.repository.PagamentoRepository;
import com.br.tools.util.PagamentosUtil;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

	private final PagamentoRepository pagamentoRepository;

	@Autowired
	public PagamentoService(PagamentoRepository pagamentoRepository) {
		this.pagamentoRepository = pagamentoRepository;
	}

	public Pagamento efetuarPagamento(Pagamento pagamento) {
		pagamento.getTransacao().getDescricao().setNsu(PagamentosUtil.gerarNSU());
		pagamento.getTransacao().getDescricao().setCodigoAutorizacao(PagamentosUtil.gerarCodigoAutorizacao());

		// Parcelas serão permitidas apenas para compras acima de 200 reais
		if (Float.parseFloat(pagamento.getTransacao().getDescricao().getValor()) <= 200
				&& Integer.parseInt(pagamento.getTransacao().getFormaPagamento().getParcelas()) > 1) {
			pagamento.getTransacao().getDescricao().setStatus("NEGADO");
		} else {
			pagamento.getTransacao().getDescricao().setStatus("AUTORIZADO");
		}
		return pagamentoRepository.save(pagamento);
	}

	public Pagamento efetuarEstorno(Pagamento pagamento) {
		pagamento.getTransacao().getDescricao().setNsu(PagamentosUtil.gerarNSU());
		pagamento.getTransacao().getDescricao().setCodigoAutorizacao(PagamentosUtil.gerarCodigoAutorizacao());

		// Nega o estorno caso já tenha se passado um período de 48 horas
		if (PagamentosUtil.passaram48HorasDesde(pagamento.getTransacao().getDescricao().getDataHora())) {
			pagamento.getTransacao().getDescricao().setStatus("NEGADO");
		} else {
			pagamento.getTransacao().getDescricao().setStatus("CANCELADO");
		}
		return pagamentoRepository.save(pagamento);
	}

	public boolean verificarCartaoJaUtilizado(String cartao, String nsu) {
		Optional<Pagamento> pagamentoExistente = pagamentoRepository.findByCartaoNsu(cartao, nsu);
		return pagamentoExistente.isPresent();
	}

	public boolean verificarIdExistente(String id) {
		Optional<Pagamento> pagamentoExistente = pagamentoRepository.findById(id);
		return pagamentoExistente.isPresent();
	}

	public List<Pagamento> buscarTodosPagamentos() {
		return pagamentoRepository.findAll();
	}

	public Optional<Pagamento> buscarPagamentoPorId(String id) {
		return pagamentoRepository.findById(id);
	}

	public Optional<Pagamento> buscarEstornosPorId(String id) {
		return pagamentoRepository.findByIdAndStatus(id, "CANCELADO");
	}

	public List<Pagamento> buscarPagamentosPorStatus(String status) {
		return pagamentoRepository.findByDescricaoStatus(status);
	}

}
