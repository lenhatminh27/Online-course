package com.course.service;

import com.course.dto.intergration.CheckTransaction;
import com.course.dto.intergration.Transaction;
import com.course.dto.request.ProcessQrRequest;
import com.course.dto.request.WebhooksRequest;
import com.course.dto.intergration.ProcessQrResponse;
import com.course.dto.intergration.WalletResponse;

public interface WalletService {

    ProcessQrResponse generateQr(ProcessQrRequest processQrRequest);

    WalletResponse getWallet();

//    void authenticateTransactional(WebhooksRequest webhooksRequest);

    CheckTransaction checkTransaction(Transaction transaction, Long tranId);

}
