package com.adev.gather.service;

import com.adev.common.base.service.BaseService;
import com.adev.gather.domain.CurrencyPair;
import org.springframework.data.domain.Page;

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

    /**
     * 分页查询交易对
     * @param pairName
     * @param exchangeName
     * @param currencySymbol
     * @param page
     * @param size
     * @param sort
     * @return
     */
    Page<CurrencyPair> searchCurrencyPair(String pairName, String exchangeName, String currencySymbol, Integer page, Integer size, String sort);
}
