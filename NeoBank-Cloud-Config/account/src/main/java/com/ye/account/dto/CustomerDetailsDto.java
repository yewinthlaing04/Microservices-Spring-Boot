package com.ye.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
@Schema( name= "Customer Details" ,
description = "Schema to hold customer , loan and card details")
public class CustomerDetailsDto {

    @Schema(
            description = "Name of the Customer" , example = "Ye Wint Hlaing"
    )
    @NotEmpty( message = " Name cannot be empty ")
    @Size( min = 5 , max = 30 , message = "The length of the customer name should be between 5 and 30")
    private String name;

    @Schema(
            description = "Email address of the customer", example = "tutor@neobank.com"
    )
    @NotEmpty(message = "Email address can not be a null or empty")
    @Email(message = "Email address should be a valid value")
    private String email;

    @Schema(
            description = "Mobile Number of the customer", example = "9345432123"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Account details of the customer"
    )
    private AccountsDto accountsDto;

    @Schema(
            description = " Card details of the customer"
    )
    private CardDto cardDto;

    @Schema(
            description = " Loan details of the customer "
    )
    private LoansDto loansDto;
}
