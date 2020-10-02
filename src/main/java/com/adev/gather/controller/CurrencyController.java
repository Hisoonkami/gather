package com.adev.gather.controller;

import com.adev.common.base.domian.BaseResult;
import com.adev.gather.service.CurrencyPairService;
import com.adev.gather.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping(value = {"/api/currencies"})
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BaseResult> addCurrency(@RequestParam(value = "name")String name,
                                                  @RequestParam(value = "symbol")String symbol,
                                                  @RequestParam(value = "issuetime",required = false)String issuetime,
                                                  @RequestParam(value = "issueprice",required = false)String issueprice,
                                                  @RequestParam(value = "website",required = false)String website,
                                                  @RequestParam(value = "explorer",required = false)String explorer,
                                                  @RequestParam(value = "whitepaper",required = false)String whitepaper,
                                                  @RequestParam(value = "offcialWallet",required = false)String offcialWallet,
                                                  @RequestParam(value = "algorithm",required = false)String algorithm,
                                                  @RequestParam(value = "proof_type",required = false)String proof_type,
                                                  @RequestParam(value = "logo",required = false)String logo,
                                                  @RequestParam(value = "total_supply",required = false)BigDecimal total_supply,
                                                  @RequestParam(value = "max_supply",required = false)BigDecimal max_supply){
        return ResponseEntity.ok(BaseResult.success(currencyService.addCurrency(name, symbol,  issuetime,  issueprice,  website,  explorer,  whitepaper,  offcialWallet,  algorithm,  proof_type,  logo,  total_supply, max_supply)));
    }

    @RequestMapping(value = {"/{id}"},method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<BaseResult> updateCurrency(@PathVariable(value = "id") Long id,
                                                     @RequestParam(value = "name")String name,
                                                     @RequestParam(value = "symbol")String symbol,
                                                     @RequestParam(value = "issuetime",required = false)String issuetime,
                                                     @RequestParam(value = "issueprice",required = false)String issueprice,
                                                     @RequestParam(value = "website",required = false)String website,
                                                     @RequestParam(value = "explorer",required = false)String explorer,
                                                     @RequestParam(value = "whitepaper",required = false)String whitepaper,
                                                     @RequestParam(value = "offcialWallet",required = false)String offcialWallet,
                                                     @RequestParam(value = "algorithm",required = false)String algorithm,
                                                     @RequestParam(value = "proof_type",required = false)String proof_type,
                                                     @RequestParam(value = "logo",required = false)String logo,
                                                     @RequestParam(value = "total_supply",required = false)BigDecimal total_supply,
                                                     @RequestParam(value = "max_supply",required = false)BigDecimal max_supply){
        return ResponseEntity.ok(BaseResult.success(currencyService.updateCurrency(id,name, symbol,  issuetime,  issueprice,  website,  explorer,  whitepaper,  offcialWallet,  algorithm,  proof_type,  logo,  total_supply, max_supply)));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BaseResult> findAllCurrency(){
        return ResponseEntity.ok(BaseResult.success(currencyService.findAll().iterator()));
    }
}
