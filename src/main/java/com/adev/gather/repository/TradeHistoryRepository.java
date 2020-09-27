package com.adev.gather.repository;

import com.adev.gather.domain.TradeHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TradeHistoryRepository extends MongoRepository<TradeHistory,ObjectId> {
    TradeHistory findFirstByExchangeAndTradeId(String exchange,Long tradeId);
}
