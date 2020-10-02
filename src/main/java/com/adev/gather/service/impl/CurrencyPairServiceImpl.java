package com.adev.gather.service.impl;

import com.adev.common.base.service.impl.BaseServiceImpl;
import com.adev.common.base.utils.PageUtils;
import com.adev.gather.domain.Currency;
import com.adev.gather.domain.CurrencyPair;
import com.adev.gather.domain.Exchange;
import com.adev.gather.repository.CurrencyPairRepository;
import com.adev.gather.service.CurrencyPairService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
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

    @Override
    public Page<CurrencyPair> searchCurrencyPair(String pairName, String exchangeName, String currencySymbol, Integer page, Integer size, String sort) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isEmpty(pairName)){
                    predicates.add(criteriaBuilder.like(root.get("pairName"), "%"+pairName+"%"));
                }
                if(!StringUtils.isEmpty(exchangeName)) {
                    Join<CurrencyPair, Exchange> join = root.join("exchange", JoinType.INNER);
                    if(null!=join) {
                        predicates.add(criteriaBuilder.like(join.get("name").as(String.class), "%"+exchangeName+"%"));
                    }
                }
                if(!StringUtils.isEmpty(currencySymbol)) {
                    Join<CurrencyPair, Currency> join = root.join("currency", JoinType.INNER);
                    if(null!=join) {
                        predicates.add(criteriaBuilder.like(join.get("symbol").as(String.class), "%"+currencySymbol+"%"));
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        PageRequest pageRequest = PageUtils.pageRequest(page,size,sort);
        Page<CurrencyPair> pageInfo=currencyPairRepository.findAll(specification,pageRequest);
        return pageInfo;
    }
}
