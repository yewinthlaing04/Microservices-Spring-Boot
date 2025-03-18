package com.eazybytes.accounts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;

@ConfigurationProperties( prefix = "accounts" )
@Getter
@Setter
public class AccountsContactInfoDto {
    private String message ;
    private Map<String , String > contactDetails ;
    private List<String> onCallSupport ;
}
