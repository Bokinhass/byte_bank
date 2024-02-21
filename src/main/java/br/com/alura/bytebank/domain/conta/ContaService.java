package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class ContaService {

  private final ConnectionFactory connection;
  private final Set<Conta> contas = new HashSet<>();

  public ContaService() {
    this.connection = new ConnectionFactory();
  }

  public Set<Conta> listarContasAbertas() {
    Connection conn = connection.recuperarConexao();
    return new ContaDAO(conn).listar();
  }

  public BigDecimal consultarSaldo(Integer numeroDaConta) {
    var conta = buscarContaPorNumero(numeroDaConta);
    return conta.getSaldo();
  }

  public void abrir(DadosAberturaConta dadosDaConta) {
    Connection conn = connection.recuperarConexao();

    new ContaDAO(conn).salvar(dadosDaConta);
  }

  public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
    var conta = buscarContaPorNumero(numeroDaConta);
    if (valor.compareTo(BigDecimal.ZERO) <= 0) {
      throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
    }

    if (valor.compareTo(conta.getSaldo()) > 0) {
      throw new RegraDeNegocioException("Saldo insuficiente!");
    }

    conta.sacar(valor);
  }

  public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
    var conta = buscarContaPorNumero(numeroDaConta);
    if (valor.compareTo(BigDecimal.ZERO) <= 0) {
      throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
    }

    conta.depositar(valor);
  }

  public void encerrar(Integer numeroDaConta) {
    var conta = buscarContaPorNumero(numeroDaConta);
    if (conta.possuiSaldo()) {
      throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
    }

    contas.remove(conta);
  }

  public Conta buscarContaPorNumero(Integer numeroDaConta) {
    Connection conn = connection.recuperarConexao();
    Conta conta = new ContaDAO(conn).listarUmaConta(numeroDaConta);

    if (conta != null) {
      return conta;

    } else {
      throw new RegraDeNegocioException("Não existe conta com esse número.");
    }
  }
}
