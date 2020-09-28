package com.adev.gather.repository;

import com.adev.gather.domain.KlineHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KlineHistoryRepository extends MongoRepository<KlineHistory,ObjectId> {
    KlineHistory findFirstByExchangeAndCurrencyPairAndTimestamp(String exchange,String currencyPair,Long timestamp);
}
