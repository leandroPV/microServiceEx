package br.com.vaneli.api.filters;

import br.com.vaneli.api.domain.QContactDomain;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactFilter {

  private String value;

  private ZonedDateTime startCreated;

  private ZonedDateTime endCreated;

  public Predicate toPredicate() {
    QContactDomain contactDomain = QContactDomain.contactDomain;
    List<Predicate> predicates = Lists.newArrayList();

    if (Objects.nonNull(this.startCreated)) {
      predicates.add(contactDomain.createdDate.goe(this.startCreated));
    }

    if (Objects.nonNull(this.endCreated)) {
      predicates.add(contactDomain.createdDate.loe(this.endCreated));
    }

    if (!Strings.isNullOrEmpty(this.value)) {
      predicates.add(contactDomain.value.eq(this.value));
    }

    return ExpressionUtils.allOf(predicates);
  }

}
