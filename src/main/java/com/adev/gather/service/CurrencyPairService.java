package com.adev.gather.service;

import com.adev.common.base.service.BaseService;
import com.adev.gather.domain.CurrencyPair;

import java.util.List;

public interface CurrencyPairService extends BaseService<CurrencyPair,Long> {
    /**
     * 添加交易对信息
     * @param exchange
     * @param pairName
     * @param currency
     * @param counterName
     * @return
     */
    Long addCurrencyPair(String exchange, String pairName, String currency, String counterName);

    /**
     * 更新交易对信息
     * @param id
     * @param exchange
     * @param pairName
     * @param currency
     * @param counterName
     * @return
     */
    boolean updateCurrencyPair(Long id,String exchange, String pairName, String currency, String counterName);

    /**
     * 根据交易对查询
     * @param exchange
     * @return
     */
    List<CurrencyPair> findByExchange(String exchange);
}
