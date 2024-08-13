package com.ye.loan.service.impl;

import com.ye.loan.constants.LoansConstants;
import com.ye.loan.dto.LoansDto;
import com.ye.loan.entity.Loans;
import com.ye.loan.exception.LoanAlreadyExistsException;
import com.ye.loan.exception.ResourceNotFoundException;
import com.ye.loan.mapper.LoanMapper;
import com.ye.loan.repository.LoanRepository;
import com.ye.loan.service.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoanService implements ILoanService {

    private LoanRepository loanRepository;

    @Override
    public void createLoan(String mobileNumber) {

        Optional<Loans> optionalLoans = loanRepository.findByMobileNumber(mobileNumber);
        if ( optionalLoans.isPresent() ) {
            throw new LoanAlreadyExistsException( "Loan already registered with given mobile Number " + mobileNumber);
        }
        loanRepository.save(createNewLoan(mobileNumber));
    }

    private Loans createNewLoan(String mobileNumber){
        Loans newLoan = new Loans();
        long randomNumber =  100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);

        return newLoan;
    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan","Mobile Number", mobileNumber)
        );

        return LoanMapper.mapToLoansDto(loans , new LoansDto());
    }

    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loanRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan","Loan Number", loansDto.getLoanNumber())
        );
        LoanMapper.mapToLoans(loansDto , loans);
        loanRepository.save(loans);
        return true;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan","mobileNumber",mobileNumber)
        );

        loanRepository.deleteById(loans.getLoanId());

        return true;
    }
}
