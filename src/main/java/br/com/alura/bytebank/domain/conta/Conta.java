package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.util.Objects;

public class Conta {

  private final Integer numero;
  private final Cliente titular;
  private final BigDecimal saldo;

  public Conta(Integer numero, BigDecimal saldo, Cliente titular) {
    this.numero = numero;
    this.titular = titular;
    this.saldo = saldo;
  }

  public boolean possuiSaldo() {
    return this.saldo.compareTo(BigDecimal.ZERO) != 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Conta conta = (Conta) o;
    return numero.equals(conta.numero);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numero);
  }

  @Override
  public String toString() {
    return "Conta{" +
        "numero='" + numero + '\'' +
        ", saldo=" + saldo +
        ", titular=" + titular +
        '}';
  }

  public Integer getNumero() {
    return numero;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

  public Cliente getTitular() {
    return titular;
  }
}
