package com.adev.gather.repository;

import com.adev.gather.domain.CurrencyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CurrencyPairRepository extends JpaRepository<CurrencyPair, Long>, JpaSpecificationExecutor {

    /**
     * 根据交易所查询交易对
     * @param exchange
     * @return
     */
    List<CurrencyPair> findByExchange(String exchange);
}
