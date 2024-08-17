package com.ye.account.controller;

import com.ye.account.constants.AccountConstants;
import com.ye.account.dto.AccountsContactInfoDto;
import com.ye.account.dto.CustomerDto;
import com.ye.account.dto.ResponseDto;
import com.ye.account.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "CRUD REST APIs for Accounts in NeoBank",
        description = "CRUD REST APIs in NeoBank to CREATE,UPDATE,FETCH AND DELETE account details"
)
@RestController
@RequestMapping(path="/api" , produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountController {

    private final  IAccountService iAccountsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    public AccountController( IAccountService iAccountsService ) {
        this.iAccountsService = iAccountsService;
    }

    // create customer and account
    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer & Accounts inside NeoBank"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount (@Valid @RequestBody CustomerDto customerDto) {
        // create customer with service
        iAccountsService.createAccount(customerDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountConstants.STATUS_201 , AccountConstants.MESSAGE_201));
    }

    // fetch account and customer from db
    @Operation(
            summary = "Fetch  Account REST API",
            description = "REST API to fetch Customer and Account inside NeoBank"
    )
    @GetMapping("/account")
    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK"
    )
    public ResponseEntity<CustomerDto> fetchAccountDetao(
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})",
                    message="Mobile Number must be 10 digits")
            String mobileNumber){

        CustomerDto customerDto =  iAccountsService.fetchCustomer(mobileNumber);

        return ResponseEntity.status(HttpStatus.OK)
                .body(customerDto);
    }

    // update controller
    @Operation(
            summary="Update Account REST API ",
            description = "REST API to update Customer & Account inside NeoBank "
    )
    @PostMapping("/account/update")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                responseCode = "417",
                description = "Expectation Success"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )}
    )
    public ResponseEntity<ResponseDto> updateAccount (@Valid @RequestBody CustomerDto customerDto){

        boolean isUpdate = iAccountsService.updateAccount(customerDto);
        if ( isUpdate ){
            return ResponseEntity.status( HttpStatus.OK)
                    .body(new ResponseDto(AccountConstants.STATUS_200 , AccountConstants.MESSAGE_200));
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountConstants.STATUS_417 , AccountConstants.MESSAGE_417_UPDATE));
        }
    }


    // delete account controller
    @Operation(
            summary = "DELETE Account REST API",
            description = "REST API to delete Customer & Account inside NeoBank"
    )
    @DeleteMapping("/account/delete")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
              responseCode = "417",
              description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )}
    )
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                               @Pattern(regexp = "(^$|[0-9]{10})",
                                                               message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_DELETE));
        }
    }
    @Operation(
            summary = "Get Java Version",
            description = "Get Java Version information that is deployed into account Microservices"
    )
    @GetMapping("/java-version")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status "
            )
    })
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(environment.getProperty("MAVEN_HOME"));
    }

    @Operation(
            summary = "Get Build Information",
            description = "Get Build Version information that is deployed into account Microservices"
    )
    @GetMapping("/build-info")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status "
            )
    })
    public ResponseEntity<String> getBuildInfo(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Get Contact Information",
            description = "Get Contact Version information that is deployed into account Microservices"
    )
    @GetMapping("/contact-info")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status "
            )
    })
    public ResponseEntity<AccountsContactInfoDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }
}

