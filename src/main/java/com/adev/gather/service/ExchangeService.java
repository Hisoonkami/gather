package com.adev.gather.service;

import com.adev.common.base.service.BaseService;
import com.adev.gather.domain.CurrencyPair;
import com.adev.gather.domain.Exchange;

import java.util.List;

public interface ExchangeService extends BaseService<Exchange,Long> {
    /**
     * 添加交易对信息
     * @param name
     * @param showName
     * @param level
     * @param logo
     * @param website
     * @param setupYear
     * @return
     */
    Long addExchange(String name, String showName, String level, String logo,String website,String setupYear);

    /**
     * 更新交易所
     * @param id
     * @param name
     * @param showName
     * @param level
     * @param logo
     * @param website
     * @param setupYear
     * @return
     */
    boolean updateExchange(Long id,String name, String showName, String level, String logo,String website,String setupYear);

}
