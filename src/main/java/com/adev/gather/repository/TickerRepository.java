package com.adev.gather.repository;

import com.adev.gather.domain.PairTicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TickerRepository extends JpaRepository<PairTicker, String> {

}
