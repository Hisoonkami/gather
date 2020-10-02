package com.adev.gather.service;

import com.adev.common.base.service.BaseService;
import com.adev.gather.domain.Currency;
import com.adev.gather.domain.CurrencyPair;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyService extends BaseService<Currency,Long> {
    /**
     * 添加币种信息
     * @param name
     * @param symbol
     * @param issuetime
     * @param issueprice
     * @param website
     * @param explorer
     * @param whitepaper
     * @param offcialWallet
     * @param algorithm
     * @param proof_type
     * @param logo
     * @param total_supply
     * @param max_supply
     * @return
     */
    Long addCurrency(String name, String symbol, String issuetime, String issueprice, String website, String explorer, String whitepaper, String offcialWallet, String algorithm, String proof_type, String logo, BigDecimal total_supply,BigDecimal max_supply);

    /**
     * 更新币种信息
     * @param id
     * @param name
     * @param symbol
     * @param issuetime
     * @param issueprice
     * @param website
     * @param explorer
     * @param whitepaper
     * @param offcialWallet
     * @param algorithm
     * @param proof_type
     * @param logo
     * @param total_supply
     * @param max_supply
     * @return
     */
    boolean updateCurrency(Long id,String name, String symbol, String issuetime, String issueprice, String website, String explorer, String whitepaper, String offcialWallet, String algorithm, String proof_type, String logo, BigDecimal total_supply,BigDecimal max_supply);

}
