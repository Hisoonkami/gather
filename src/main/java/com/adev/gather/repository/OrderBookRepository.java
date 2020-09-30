package com.adev.gather.repository;

import com.adev.gather.domain.PairOrderBook;
import com.adev.gather.domain.PairTicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface OrderBookRepository extends JpaRepository<PairOrderBook, String> {

}
