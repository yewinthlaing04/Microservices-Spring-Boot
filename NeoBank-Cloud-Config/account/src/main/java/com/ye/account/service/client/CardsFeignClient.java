package com.ye.account.service.client;

import com.ye.account.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards")
public interface CardsFeignClient {

    @GetMapping(value = "/api/card/fetch", consumes="application/json")
    public ResponseEntity<CardDto> fetchCardDetails(@RequestParam String mobileNumber);
}
