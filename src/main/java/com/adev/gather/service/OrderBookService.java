package com.adev.gather.service;

import com.adev.common.base.service.BaseService;
import com.adev.gather.domain.PairOrderBook;
import com.adev.gather.domain.PairTicker;

public interface OrderBookService extends BaseService<PairOrderBook,String> {

    /**
     * 根据交易所和交易对查找OrderBook
     * @param exchange
     * @param pairName
     * @return
     */
    PairOrderBook findByExchangeAndPairName(String exchange, String pairName);
}
