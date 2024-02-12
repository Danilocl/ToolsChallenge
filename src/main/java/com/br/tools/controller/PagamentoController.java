package com.br.tools.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.br.tools.exception.ErrorResponse;
import com.br.tools.exception.GenericExpression;
import com.br.tools.model.Pagamento;
import com.br.tools.service.PagamentoService;
import com.br.tools.util.PagamentosUtil;

@RestController
@RequestMapping("/api")
public class PagamentoController {
	@Autowired
	private PagamentoService pagamentoService;

	@PostMapping("/autorizar")
	public ResponseEntity<?> autorizarPagamento(@RequestBody Pagamento pagamento) {
		try {
			// Verifica se o ID já existe
			if (pagamento.getTransacao().getId() != null
					&& pagamentoService.verificarIdExistente(pagamento.getTransacao().getId())) {
				throw new GenericExpression("ID já existente.");
			}

			// Verifica se o tipo de pagamento é válido
			if (pagamento.getTransacao().getFormaPagamento().getTipo() != null
					&& PagamentosUtil.validaTipo(pagamento.getTransacao().getFormaPagamento().getTipo())) {
				throw new GenericExpression("Tipo Inválido");
			}

			if (!PagamentosUtil.verificarTamanho(pagamento.getTransacao().getCartao())) {
				throw new GenericExpression("O cartão precisa ter 13 ou 16 dígitos");
			}

			Pagamento response = pagamentoService.efetuarPagamento(pagamento);
			response.getTransacao().setCartao(PagamentosUtil.criptografaCartao(pagamento.getTransacao().getCartao()));
			return ResponseEntity.ok(response);
		} catch (GenericExpression e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@PostMapping("/estorno")
	public ResponseEntity<?> estornarPagamento(@RequestBody Pagamento pagamento) {
		try {
			// Verifica se o ID já existe
			if (pagamento.getTransacao().getId() != null
					&& pagamentoService.verificarIdExistente(pagamento.getTransacao().getId())) {
				throw new GenericExpression("ID já existente.");
			}

			// Verifica se o tipo de pagamento é válido
			if (pagamento.getTransacao().getFormaPagamento().getTipo() != null
					&& PagamentosUtil.validaTipo(pagamento.getTransacao().getFormaPagamento().getTipo())) {
				throw new GenericExpression("Tipo Inválido");
			}

			if (pagamento.getTransacao().getCartao() != null && pagamentoService.verificarCartaoJaUtilizado(
					pagamento.getTransacao().getCartao(), pagamento.getTransacao().getDescricao().getNsu())) {
				Pagamento response = pagamentoService.efetuarEstorno(pagamento);
				response.getTransacao()
						.setCartao(PagamentosUtil.criptografaCartao(pagamento.getTransacao().getCartao()));
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.badRequest().body(new ErrorResponse("Cartão não cadastrado"));
			}

		} catch (GenericExpression e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@GetMapping("/estorno/{id}")
	public ResponseEntity<?> buscarestornoId(@PathVariable String id) {
		Optional<Pagamento> response = pagamentoService.buscarEstornosPorId(id);
		if (response.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new ErrorResponse("Não foi encontrado nenhum estorno referente ao ID informado"));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/pagamento/{id}")
	public ResponseEntity<?> buscarPagamentoId(@PathVariable String id) {
		Optional<Pagamento> response = pagamentoService.buscarPagamentoPorId(id);
		if (response.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new ErrorResponse("Não foi encontrado nenhum pagamento referente ao ID informado"));
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/todos")
	public ResponseEntity<?> buscarPagamentos() {
		List<Pagamento> response = pagamentoService.buscarTodosPagamentos();
		if (response.isEmpty()) {
			return ResponseEntity.badRequest().body(new ErrorResponse("Nenhuma transação efetuada"));
		}

		return ResponseEntity.ok(response);
	}

}
