package com.adev.gather.controller;

import com.adev.common.base.domian.BaseResult;
import com.adev.gather.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/api/exchanges"})
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BaseResult> addExchange(@RequestParam(value = "name")String name,
                                                  @RequestParam(value = "showName")String showName,
                                                  @RequestParam(value = "level",required = false)String level,
                                                  @RequestParam(value = "logo",required = false)String logo,
                                                  @RequestParam(value = "website",required = false)String website,
                                                  @RequestParam(value = "setupYear",required = false)String setupYear){
        return ResponseEntity.ok(BaseResult.success(exchangeService.addExchange(name,showName,level,logo,website,setupYear)));
    }

    @RequestMapping(value = {"/{id}"},method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<BaseResult> updateExchange(@PathVariable(value = "id") Long id,
                                                     @RequestParam(value = "name",required = false)String name,
                                                     @RequestParam(value = "showName",required = false)String showName,
                                                     @RequestParam(value = "level",required = false)String level,
                                                     @RequestParam(value = "logo",required = false)String logo,
                                                     @RequestParam(value = "website",required = false)String website,
                                                     @RequestParam(value = "setupYear",required = false)String setupYear){
        return ResponseEntity.ok(BaseResult.success(exchangeService.updateExchange(id,name,showName,level,logo,website,setupYear)));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BaseResult> findAllExchange(){
        return ResponseEntity.ok(BaseResult.success(exchangeService.findAll().iterator()));
    }
}
