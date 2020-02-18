package br.com.vaneli.api.domain;

import br.com.vaneli.api.interfaces.json.Address;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressDomain {

  @Id
  @Column(name = "tx_id_address")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "tx_bairro")
  private String bairro;

  @Column(name = "tx_cidade")
  private String cidade;

  @Column(name = "tx_logradouro")
  private String logradouro;

  @Column(name = "tx_estado")
  private String estado;

  @OneToOne
  @JoinColumn(name = "tx_id_user")
  private UserDomain user;

  public Address toAddress() {
    return Address.builder()
      .bairro(this.bairro)
      .cidade(this.cidade)
      .estado(this.estado)
      .logradouro(this.logradouro)
      .build();
  }

}
