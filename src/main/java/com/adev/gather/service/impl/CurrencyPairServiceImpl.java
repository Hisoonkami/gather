package com.adev.gather.service.impl;

import com.adev.common.base.service.impl.BaseServiceImpl;
import com.adev.gather.domain.CurrencyPair;
import com.adev.gather.repository.CurrencyPairRepository;
import com.adev.gather.service.CurrencyPairService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyPairServiceImpl extends BaseServiceImpl<CurrencyPair, Long> implements CurrencyPairService {

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    @Override
    @Transactional
    public Long addCurrencyPair(String exchange, String pairName, String currency, String counterName) {
        CurrencyPair currencyPair=new CurrencyPair();
        currencyPair.setExchange(exchange);
        currencyPair.setPairName(pairName);
        currencyPair.setCounterName(counterName);
        currencyPair.setCurrency(currency);
        currencyPairRepository.save(currencyPair);
        return currencyPair.getId();
    }

    @Override
    @Transactional
    public boolean updateCurrencyPair(Long id, String exchange, String pairName, String currency, String counterName) {
        CurrencyPair currencyPair=currencyPairRepository.findById(id).orElse(null);
        if(null!=currencyPair){
            if(StringUtils.isNotBlank(currency)){
                currencyPair.setCurrency(currency);
            }
            if(StringUtils.isNotBlank(pairName)){
                currencyPair.setPairName(pairName);
            }
            if(StringUtils.isNotBlank(exchange)){
                currencyPair.setExchange(exchange);
            }
            if(StringUtils.isNotBlank(counterName)){
                currencyPair.setCounterName(counterName);
            }
            currencyPairRepository.save(currencyPair);
            return true;
        }
        return false;
    }

    @Override
    public List<CurrencyPair> findByExchange(String exchange) {
        return currencyPairRepository.findByExchange(exchange);
    }
}
