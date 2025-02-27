package com.course.dto.intergration;

import lombok.Data;

import java.util.List;

@Data
public class TransactionSepayResponse {
    private int status;
    private String error;
    private Messages messages;
    private List<Transaction> transactions;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Messages getMessages() {
        return messages;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    static class Messages {
        private boolean success;

        public boolean isSuccess() {
            return success;
        }
    }
}

