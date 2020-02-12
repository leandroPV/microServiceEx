package br.com.vaneli.api.domain;

import static com.google.common.collect.Lists.newArrayList;

import br.com.vaneli.api.interfaces.json.User;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

  @Column(name = "tx_cpf", nullable = false)
  private String cpf;

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  private List<ContactDomain> contacts = newArrayList();

  public User toUser() {
    return User.builder()
      .id(this.id)
      .name(this.name)
      .cpf(this.cpf)
      .build();
  }

}
