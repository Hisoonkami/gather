package com.adev.gather.service.impl;

import com.adev.common.base.service.impl.BaseServiceImpl;
import com.adev.gather.domain.Currency;
import com.adev.gather.domain.CurrencyPair;
import com.adev.gather.repository.CurrencyPairRepository;
import com.adev.gather.repository.CurrencyRepository;
import com.adev.gather.service.CurrencyPairService;
import com.adev.gather.service.CurrencyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CurrencyServiceImpl extends BaseServiceImpl<Currency, Long> implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        super(currencyRepository);
        this.currencyRepository = currencyRepository;
    }

    @Override
    @Transactional
    public Long addCurrency(String name, String symbol, String issuetime, String issueprice, String website, String explorer, String whitepaper, String offcialWallet, String algorithm, String proof_type, String logo, BigDecimal total_supply, BigDecimal max_supply) {
        if(!existName(null,name)){
            Currency currency=new Currency();
            currency.setName(name);
            currency.setSymbol(symbol);
            currency.setIssuetime(issuetime);
            currency.setIssueprice(issueprice);
            currency.setWebsite(website);
            currency.setExplorer(explorer);
            currency.setWhitepaper(whitepaper);
            currency.setOffcialWallet(offcialWallet);
            currency.setAlgorithm(algorithm);
            currency.setProof_type(proof_type);
            currency.setLogo(logo);
            currency.setTotal_supply(total_supply);
            currency.setMax_supply(max_supply);
            currencyRepository.save(currency);
            return currency.getId();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean updateCurrency(Long id, String name, String symbol, String issuetime, String issueprice, String website, String explorer, String whitepaper, String offcialWallet, String algorithm, String proof_type, String logo, BigDecimal total_supply, BigDecimal max_supply) {
        Currency currency=currencyRepository.findById(id).orElse(null);
        if(null!=currency&&!existName(id,name)){
            if(StringUtils.isNotBlank(name)){
                currency.setName(name);
            }
            if(StringUtils.isNotBlank(symbol)){
                currency.setSymbol(symbol);
            }
            if(StringUtils.isNotBlank(issuetime)){
                currency.setIssuetime(issuetime);
            }
            if(StringUtils.isNotBlank(issueprice)){
                currency.setIssueprice(issueprice);
            }
            if(StringUtils.isNotBlank(website)){
                currency.setWebsite(website);
            }
            if(StringUtils.isNotBlank(explorer)){
                currency.setExplorer(explorer);
            }
            if(StringUtils.isNotBlank(whitepaper)){
                currency.setWhitepaper(whitepaper);
            }
            if(StringUtils.isNotBlank(offcialWallet)){
                currency.setOffcialWallet(offcialWallet);
            }
            if(StringUtils.isNotBlank(algorithm)){
                currency.setAlgorithm(algorithm);
            }
            if(StringUtils.isNotBlank(proof_type)){
                currency.setProof_type(proof_type);
            }
            if(StringUtils.isNotBlank(logo)){
                currency.setLogo(logo);
            }
            if(null!=total_supply){
                currency.setTotal_supply(total_supply);
            }
            if(null!=max_supply){
                currency.setMax_supply(max_supply);
            }
            currencyRepository.save(currency);
            return true;
        }
        return false;
    }

    private boolean existName(Long id,String name){
        Currency currency=currencyRepository.findFirstByName(name);
        if(null!=currency&&(null==id||!id.equals(currency.getId()))){
            return true;
        }
        return false;
    }
}
