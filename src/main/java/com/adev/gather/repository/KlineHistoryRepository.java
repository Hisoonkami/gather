package com.adev.gather.repository;

import com.adev.gather.domain.KlineHistory;
import com.adev.gather.domain.TradeHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface KlineHistoryRepository extends MongoRepository<KlineHistory,ObjectId> {
    TradeHistory findFirstByExchangeAndTradeId(String exchange, Long tradeId);
}
