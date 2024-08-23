package com.ye.account.controller;

import com.ye.account.dto.CustomerDetailsDto;
import com.ye.account.dto.ErrorResponseDto;
import com.ye.account.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/api" , produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Fetch Customer Details REST api" ,
            description = " REST API to fetch Customer Details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("neoBank-correlation-id") String correlationId,
            @RequestParam
            @Pattern( regexp = "(^$|[0-9]{10})" , message = "Mobile number must be 10 digits" )
            String mobileNumber
    ) {

        logger.debug("neoBank-correlation-id: {}", correlationId);

        CustomerDetailsDto customerDetailsDto = customerService.fetchCustomerDetails(mobileNumber , correlationId);

        return ResponseEntity.status(HttpStatus.OK ).body(customerDetailsDto);
    }
}
