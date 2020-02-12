package br.com.vaneli.api.domain;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@MappedSuperclass
public abstract class AuditDomain {

  @UpdateTimestamp
  @Column(name = "dt_updated", insertable = false)
  protected ZonedDateTime updatedDate;

  @CreationTimestamp
  @Column(name = "dt_created", updatable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
  protected ZonedDateTime createdDate;
}
