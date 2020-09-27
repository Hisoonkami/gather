package com.adev.gather.controller;

import com.adev.common.base.domian.BaseResult;
import com.adev.gather.service.CurrencyPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/api/currencyPairs"})
public class CurrencyPairController {

    @Autowired
    private CurrencyPairService currencyPairService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BaseResult> addCurrencyPair(@RequestParam(value = "exchange") String exchange,
                                                      @RequestParam(value = "pairName") String pairName,
                                                      @RequestParam(value = "currency") String currency,
                                                      @RequestParam(value = "counterName")String counterName){
        return ResponseEntity.ok(BaseResult.success(currencyPairService.addCurrencyPair(exchange,pairName,currency,counterName)));
    }

    @RequestMapping(value = {"/{id}"},method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<BaseResult> updateCurrencyPair(@PathVariable(value = "id") Long id,
                                                         @RequestParam(value = "exchange",required = false) String exchange,
                                                         @RequestParam(value = "pairName",required = false) String pairName,
                                                         @RequestParam(value = "currency",required = false) String currency,
                                                         @RequestParam(value = "counterName",required = false)String counterName){
        return ResponseEntity.ok(BaseResult.success(currencyPairService.updateCurrencyPair(id,exchange,pairName,currency,counterName)));
    }

    @RequestMapping(value = {"/search/findByExchange"},method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BaseResult> findByExchange(@RequestParam(value = "exchange") String exchange){
        return ResponseEntity.ok(BaseResult.success(currencyPairService.findByExchange(exchange)));
    }
}
