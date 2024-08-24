package com.ye.account.service.client;

import com.ye.account.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards" , fallback = CardsFallBack.class)
public interface CardsFeignClient {

    @GetMapping(value = "/api/card/fetch", consumes="application/json")
    public ResponseEntity<CardDto> fetchCardDetails(@RequestHeader("neoBank-correlation-id") String correlationId ,
                                                    @RequestParam String mobileNumber);
}
