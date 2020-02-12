package br.com.vaneli.api.repository;

import br.com.vaneli.api.domain.QUserDomain;
import br.com.vaneli.api.domain.UserDomain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDomain, UUID>,
  QuerydslPredicateExecutor<UserDomain>,
  QuerydslBinderCustomizer<QUserDomain> {

  @Override
  default void customize(QuerydslBindings bindings, QUserDomain root) {
  }

  boolean existsById(UUID userId);

}
