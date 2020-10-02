package com.adev.gather.repository;

import com.adev.gather.domain.CurrentKline;
import com.adev.gather.domain.CurrentTrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentTradeRepository extends JpaRepository<CurrentTrade, String> {

}
