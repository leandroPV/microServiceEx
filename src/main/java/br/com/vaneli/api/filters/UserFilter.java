package br.com.vaneli.api.filters;

import br.com.vaneli.api.domain.QUserDomain;
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
public class UserFilter {

  private String name;

  private String cpf;

  private ZonedDateTime startCreated;

  private ZonedDateTime endCreated;

  public Predicate toPredicate() {
    QUserDomain userDomain = QUserDomain.userDomain;
    List<Predicate> predicates = Lists.newArrayList();

    if (Objects.nonNull(this.startCreated)) {
      predicates.add(userDomain.createdDate.goe(this.startCreated));
    }

    if (Objects.nonNull(this.endCreated)) {
      predicates.add(userDomain.createdDate.loe(this.endCreated));
    }

    if (!Strings.isNullOrEmpty(this.name)) {
      predicates.add(userDomain.name.eq(this.name));
    }

    if (!Strings.isNullOrEmpty(this.cpf)) {
      predicates.add(userDomain.cpf.eq(this.cpf));
    }

    return ExpressionUtils.allOf(predicates);
  }

}
