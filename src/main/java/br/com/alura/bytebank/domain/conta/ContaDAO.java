package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {

  private final Connection conn;

  ContaDAO(Connection connection) {
    this.conn = connection;
  }

  public void salvar(DadosAberturaConta dadosDaConta) {
    var cliente = new Cliente(dadosDaConta.dadosCliente());
    var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente);

    String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)" +
        "VALUE (?, ?, ?, ?, ?)";

    try {
      PreparedStatement preparedStatement = conn.prepareStatement(sql);

      preparedStatement.setInt(1, conta.getNumero());
      preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
      preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
      preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
      preparedStatement.setString(5, dadosDaConta.dadosCliente().email());

      preparedStatement.execute();
      preparedStatement.close();

      conn.close();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Set<Conta> listar() {
    PreparedStatement ps;
    ResultSet rs;

    Set<Conta> contas = new HashSet<>();

    String sql = "SELECT * FROM conta";

    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();

      while (rs.next()) {
        Integer numero = rs.getInt(1);
        BigDecimal saldo = rs.getBigDecimal(2);
        String nome = rs.getString(3);
        String cpf = rs.getString(4);
        String email = rs.getString(5);

        DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);

        Cliente cliente = new Cliente(dadosCadastroCliente);

        contas.add(new Conta(numero, saldo, cliente));
      }
      rs.close();
      ps.close();
      conn.close();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return contas;
  }

  public Conta listarUmaConta(Integer numero) {
    PreparedStatement ps;
    ResultSet rs;

    Conta conta = null;

    String sql = "SELECT * FROM conta  WHERE numero = " + numero + "and esta_ativa = true";

    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, numero);
      rs = ps.executeQuery();

      while (rs.next()) {
        Integer numeroRecuperado = rs.getInt(1);
        BigDecimal saldo = rs.getBigDecimal(2);
        String nome = rs.getString(3);
        String cpf = rs.getString(4);
        String email = rs.getString(5);

        DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);

        Cliente cliente = new Cliente(dadosCadastroCliente);

        conta = new Conta(numeroRecuperado, saldo, cliente);
      }

      rs.close();
      ps.close();
      conn.close();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return conta;
  }
}
