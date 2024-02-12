package com.br.tools.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.br.tools.model.Descricao;
import com.br.tools.model.FormaPagamento;
import com.br.tools.model.Pagamento;
import com.br.tools.model.Transacao;
import com.br.tools.repository.PagamentoRepository;
import com.br.tools.service.PagamentoService;
import com.br.tools.util.PagamentosUtilMock;

@SpringBootTest
class PagamentoServiceTest {

	@Mock
	private PagamentoRepository pagamentoRepository;

	@InjectMocks
	private PagamentoService pagamentoService;

	@Test
	void testEfetuarPagamento() {
		Pagamento pagamento = criarPagamento();
		final String dataHoraTrue = "01/01/2024 18:30:00";

		PagamentosUtilMock pagamentosUtil = mock(PagamentosUtilMock.class);

		when(pagamentosUtil.gerarNSU()).thenReturn("2843099554");
		when(pagamentosUtil.gerarCodigoAutorizacao()).thenReturn("0987654321");
		when(pagamentosUtil.passaram48HorasDesde(dataHoraTrue)).thenReturn(true);

		when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

		Pagamento resultado = pagamentoService.efetuarPagamento(pagamento);

		verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

		assertNotNull(resultado);
		assertEquals("AUTORIZADO", resultado.getTransacao().getDescricao().getStatus());
	}

	@Test
	void testNegarPagamento() {
		Pagamento pagamento = criarPagamentoNegado();
		final String dataHoraTrue = "01/01/2024 18:30:00";

		PagamentosUtilMock pagamentosUtil = mock(PagamentosUtilMock.class);

		when(pagamentosUtil.gerarNSU()).thenReturn("2843099554");
		when(pagamentosUtil.gerarCodigoAutorizacao()).thenReturn("0987654321");
		when(pagamentosUtil.passaram48HorasDesde(dataHoraTrue)).thenReturn(true);

		when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

		Pagamento resultado = pagamentoService.efetuarPagamento(pagamento);

		verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

		assertNotNull(resultado);
		assertEquals("NEGADO", resultado.getTransacao().getDescricao().getStatus());
	}

	@Test
	void testEfetuarEstorno() {

		Pagamento pagamento = criarPagamento();

		PagamentosUtilMock pagamentosUtil = mock(PagamentosUtilMock.class);
		when(pagamentosUtil.gerarNSU()).thenReturn("2843099554");
		when(pagamentosUtil.gerarCodigoAutorizacao()).thenReturn("0987654321");
		when(pagamentosUtil.passaram48HorasDesde(any())).thenReturn(false);

		when(pagamentoRepository.save(any())).thenReturn(pagamento);

		Pagamento resultado = pagamentoService.efetuarEstorno(pagamento);

		verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

		assertNotNull(resultado);

		assertEquals("CANCELADO", resultado.getTransacao().getDescricao().getStatus());
	}

	@Test
	void testNegarEstorno() {

		Pagamento pagamento = criarPagamentoNegado();

		PagamentosUtilMock pagamentosUtil = mock(PagamentosUtilMock.class);
		when(pagamentosUtil.gerarNSU()).thenReturn("2843099554");
		when(pagamentosUtil.gerarCodigoAutorizacao()).thenReturn("0987654321");
		when(pagamentosUtil.passaram48HorasDesde(any())).thenReturn(false);

		when(pagamentoRepository.save(any())).thenReturn(pagamento);

		Pagamento resultado = pagamentoService.efetuarEstorno(pagamento);

		verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

		assertNotNull(resultado);

		assertEquals("NEGADO", resultado.getTransacao().getDescricao().getStatus());
	}

	@Test
	void testVerificarCartaoJaUtilizado() {

		Pagamento pagamento = criarPagamento();

		when(pagamentoRepository.findByCartaoNsu(anyString(), anyString())).thenReturn(Optional.of(pagamento));

		boolean resultado = pagamentoService.verificarCartaoJaUtilizado("44441234", "1234567890");

		verify(pagamentoRepository, times(1)).findByCartaoNsu(anyString(), anyString());

		assertTrue(resultado);
	}

	@Test
	void testVerificarIdExistente() {

		Pagamento pagamento = criarPagamento();

		when(pagamentoRepository.findById(anyString())).thenReturn(Optional.of(pagamento));

		boolean resultado = pagamentoService.verificarIdExistente("100023568900001");

		verify(pagamentoRepository, times(1)).findById(anyString());

		assertTrue(resultado);
	}

	@Test
	void testBuscarTodosPagamentos() {
		List<Pagamento> pagamentos = new ArrayList<>();
		pagamentos.add(criarPagamento());
		pagamentos.add(criarPagamentoNegado());

		when(pagamentoRepository.findAll()).thenReturn(pagamentos);

		List<Pagamento> resultado = pagamentoService.buscarTodosPagamentos();

		assertEquals(pagamentos.size(), resultado.size());
	}

	@Test
	void testBuscarPagamentoPorId() {
		Pagamento pagamento = new Pagamento();
		String id = "1";

		when(pagamentoRepository.findById(id)).thenReturn(Optional.of(pagamento));

		Optional<Pagamento> resultado = pagamentoService.buscarPagamentoPorId(id);

		assertTrue(resultado.isPresent());
		assertEquals(pagamento, resultado.get());
	}

	@Test
	void testBuscarEstornosPorId() {
		Pagamento pagamento = new Pagamento();
		String id = "1";

		when(pagamentoRepository.findByIdAndStatus(id, "CANCELADO")).thenReturn(Optional.of(pagamento));

		Optional<Pagamento> resultado = pagamentoService.buscarEstornosPorId(id);

		assertTrue(resultado.isPresent());
		assertEquals(pagamento, resultado.get());
	}

	@Test
	void testBuscarPagamentosPorStatusCancelado() {
		List<Pagamento> pagamentos = new ArrayList<>();
		pagamentos.add(criarPagamento());
		pagamentos.add(criarPagamento());

		String status = "CANCELADO";

		when(pagamentoRepository.findByDescricaoStatus(status)).thenReturn(pagamentos);

		List<Pagamento> resultado = pagamentoService.buscarPagamentosPorStatus(status);

		assertEquals(pagamentos.size(), resultado.size());
	}

	@Test
	void testBuscarPagamentosPorStatusNegado() {
		List<Pagamento> pagamentos = new ArrayList<>();
		pagamentos.add(criarPagamentoNegado());
		pagamentos.add(criarPagamentoNegado());

		String status = "NEGADO";

		when(pagamentoRepository.findByDescricaoStatus(status)).thenReturn(pagamentos);

		List<Pagamento> resultado = pagamentoService.buscarPagamentosPorStatus(status);

		assertEquals(pagamentos.size(), resultado.size());
	}

	private Pagamento criarPagamento() {
		Pagamento pagamento = new Pagamento();
		Transacao transacao = new Transacao();
		Descricao descricao = new Descricao();
		FormaPagamento formaPagamento = new FormaPagamento();
		LocalDateTime dataAtual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String dataFormatada = dataAtual.format(formatter);

		descricao.setValor("100");
		descricao.setDataHora(dataFormatada);
		descricao.setEstabelecimento("Petshop");
		descricao.setNsu("2843099554");

		formaPagamento.setParcelas("1");
		formaPagamento.setTipo("AVISTA");

		transacao.setCartao("1234567891012145");
		transacao.setDescricao(descricao);
		transacao.setFormaPagamento(formaPagamento);
		transacao.setDescricao(descricao);

		pagamento.setTransacao(transacao);

		return pagamento;
	}

	private Pagamento criarPagamentoNegado() {
		Pagamento pagamento = new Pagamento();
		Transacao transacao = new Transacao();
		Descricao descricao = new Descricao();
		FormaPagamento formaPagamento = new FormaPagamento();

		descricao.setValor("100");
		// mudando a data para passar um perído de 48 horas
		descricao.setDataHora("01/02/2024 18:30:00");
		descricao.setEstabelecimento("Petshop");
		descricao.setNsu("2843099554");

		formaPagamento.setParcelas("2");
		formaPagamento.setTipo("AVISTA");

		transacao.setCartao("1234567891012145");
		transacao.setDescricao(descricao);
		transacao.setFormaPagamento(formaPagamento);
		transacao.setDescricao(descricao);

		pagamento.setTransacao(transacao);

		return pagamento;
	}

}
