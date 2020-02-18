package br.com.vaneli.api.interfaces.json;

import br.com.vaneli.api.domain.AddressDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  private String bairro;

  private String cidade;

  private String logradouro;

  private String estado;

  public AddressDomain toAddressDomain() {
    return AddressDomain.builder()
      .bairro(this.bairro)
      .cidade(this.cidade)
      .estado(this.estado)
      .logradouro(this.logradouro)
      .build();
  }

}
