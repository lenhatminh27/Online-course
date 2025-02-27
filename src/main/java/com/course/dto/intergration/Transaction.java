package com.course.dto.intergration;

import lombok.Data;

@Data
public class Transaction {
    private String id;
    private String bank_brand_name;
    private String account_number;
    private String transaction_date;
    private String amount_out;
    private String amount_in;
    private String accumulated;
    private String transaction_content;
    private String reference_number;
    private String code;
    private String sub_account;
    private String bank_account_id;

    public String getId() {
        return id;
    }

    public String getBankBrandName() {
        return bank_brand_name;
    }

    public String getAccountNumber() {
        return account_number;
    }

    public String getTransactionDate() {
        return transaction_date;
    }

    public String getAmountOut() {
        return amount_out;
    }

    public String getAmountIn() {
        return amount_in;
    }

    public String getAccumulated() {
        return accumulated;
    }

    public String getTransactionContent() {
        return transaction_content;
    }

    public String getReferenceNumber() {
        return reference_number;
    }

    public String getCode() {
        return code;
    }

    public String getSubAccount() {
        return sub_account;
    }

    public String getBankAccountId() {
        return bank_account_id;
    }
}

