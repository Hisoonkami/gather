package com.adev.gather.service;

import com.adev.common.base.service.BaseService;
import com.adev.gather.domain.PairTicker;

public interface TickerService extends BaseService<PairTicker,String> {

    /**
     * 根据交易所和交易对查找ticker
     * @param exchange
     * @param pairName
     * @return
     */
    PairTicker findByExchangeAndPairName(String exchange,String pairName);
}
