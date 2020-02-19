package br.com.vaneli.api.domain;

import static com.google.common.collect.Lists.newArrayList;

import br.com.vaneli.api.converters.EncryptAndDecryptAE256Converter;
import br.com.vaneli.api.interfaces.enumerators.Sexo;
import br.com.vaneli.api.interfaces.json.User;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserDomain extends AuditDomain {

  @Id
  @Column(name = "tx_id_user")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "tx_name", nullable = false)
  private String name;

  @Column(name = "tx_sexo", nullable = false)
  @Enumerated(EnumType.STRING)
  private Sexo sexo;

  @Column(name = "tx_cpf", nullable = false)
  @Convert(converter = EncryptAndDecryptAE256Converter.class)
  private String cpf;

  @Column(name = "tx_phone", nullable = false)
  private String phone;

  @Column(name = "tx_cep")
  private String cep;

  @ToString.Exclude
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  private AddressDomain address;

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  private List<ContactDomain> contacts = newArrayList();

  public User toUser() {
    User user = User.builder()
      .id(this.id)
      .name(this.name)
      .cpf(this.cpf)
      .phone(this.phone)
      .cep(this.cep)
      .sexo(this.sexo)
      .build();

    if (Objects.nonNull(this.address)) {
      user.setAddress(this.address.toAddress());
    }

    return user;
  }

}
