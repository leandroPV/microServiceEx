package br.com.vaneli.api.domain;

import br.com.vaneli.api.interfaces.json.Contact;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact")
public class ContactDomain extends AuditDomain {

  @Id
  @Column(name = "tx_id_contact")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "tx_value", nullable = false)
  private String value;

  @ManyToOne(optional = false)
  @JoinColumn(name = "tx_id_user", nullable = false)
  private UserDomain user;

  public Contact toContact() {
    return Contact.builder()
      .id(this.id)
      .value(this.value)
      .build();
  }

}
