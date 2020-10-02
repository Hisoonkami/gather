package com.adev.gather.service.impl;

import com.adev.common.base.service.impl.BaseServiceImpl;
import com.adev.gather.domain.Exchange;
import com.adev.gather.repository.ExchangeRepository;
import com.adev.gather.service.ExchangeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ExchangeServiceImpl extends BaseServiceImpl<Exchange, Long> implements ExchangeService {

    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeServiceImpl(ExchangeRepository exchangeRepository) {
        super(exchangeRepository);
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    @Transactional
    public Long addExchange(String name, String showName, String level, String logo, String website, String setupYear) {
        if(!existName(null,name)){
            Exchange exchange=new Exchange();
            exchange.setName(name);
            exchange.setShowName(showName);
            exchange.setLevel(level);
            exchange.setLogo(logo);
            exchange.setWebsite(website);
            exchange.setSetupYear(setupYear);
            exchangeRepository.save(exchange);
            return exchange.getId();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean updateExchange(Long id, String name, String showName, String level, String logo, String website, String setupYear) {
        Exchange exchange=exchangeRepository.findById(id).orElse(null);
        if(null!=exchange&&!existName(id,name)){
            if(StringUtils.isNotBlank(name)){
                exchange.setName(name);
            }
            if(StringUtils.isNotBlank(showName)){
                exchange.setShowName(showName);
            }
            if(StringUtils.isNotBlank(level)){
                exchange.setLevel(level);
            }
            if(StringUtils.isNotBlank(logo)){
                exchange.setLogo(logo);
            }
            if(StringUtils.isNotBlank(website)){
                exchange.setWebsite(website);
            }
            if(StringUtils.isNotBlank(setupYear)){
                exchange.setSetupYear(setupYear);
            }
            exchangeRepository.save(exchange);
            return true;
        }
        return false;
    }

    private boolean existName(Long id,String name){
        Exchange exchange=exchangeRepository.findFirstByName(name);
        if(null!=exchange&&(null==id||!id.equals(exchange.getId()))){
            return true;
        }
        return false;
    }
}
