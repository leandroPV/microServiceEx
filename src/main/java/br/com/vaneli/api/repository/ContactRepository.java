package br.com.vaneli.api.repository;

import br.com.vaneli.api.domain.ContactDomain;
import br.com.vaneli.api.domain.QContactDomain;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactDomain, UUID>,
  QuerydslPredicateExecutor<ContactDomain>,
  QuerydslBinderCustomizer<QContactDomain> {

  @Override
  default void customize(QuerydslBindings bindings, QContactDomain root) {
  }

  Optional<ContactDomain> findByUserIdAndId(UUID userId, UUID contactId);

}
