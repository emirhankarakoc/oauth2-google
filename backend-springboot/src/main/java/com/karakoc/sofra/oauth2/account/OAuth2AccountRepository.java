package com.karakoc.sofra.oauth2.account;

import org.hibernate.query.criteria.JpaParameterExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2AccountRepository extends JpaRepository<OAuth2Account,String> {
}
