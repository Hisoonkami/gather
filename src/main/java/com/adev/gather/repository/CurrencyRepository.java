package com.adev.gather.repository;

import com.adev.gather.domain.Currency;
import com.adev.gather.domain.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findFirstByName(String name);
}
