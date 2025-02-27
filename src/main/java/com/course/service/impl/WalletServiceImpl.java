package com.course.service.impl;

import com.course.config.properties.SepayProperties;
import com.course.core.bean.annotations.Service;
import com.course.dao.AccountDAO;
import com.course.dao.TransactionDAO;
import com.course.dao.WalletDAO;
import com.course.dto.base.WsResponse;
import com.course.dto.intergration.CheckTransaction;
import com.course.dto.intergration.Transaction;
import com.course.dto.request.ProcessQrRequest;
import com.course.dto.request.WebhooksRequest;
import com.course.dto.intergration.ProcessQrResponse;
import com.course.dto.intergration.WalletResponse;
import com.course.entity.AccountEntity;
import com.course.entity.TransactionEntity;
import com.course.entity.WalletEntity;
import com.course.entity.enums.ETransaction;
import com.course.exceptions.ForbiddenException;
import com.course.security.context.AuthenticationContextHolder;
import com.course.service.WalletService;
import com.course.web.ws.PaymentResultWs;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.course.common.utils.StringUtils.generateCustomId;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private static final SepayProperties sepay = SepayProperties.getInstance();


    private final TransactionDAO transactionDAO;

    private final AccountDAO accountDAO;

    private final WalletDAO walletDAO;


    @Override
    public ProcessQrResponse generateQr(ProcessQrRequest processQrRequest) {
//        String qr = "https://qr.sepay.vn/img?acc=0869953175&bank=MBBank&amount=5000&des=NOI_DUNG&template=TEMPLATE&download=DOWNLOAD";
        StringBuilder desc = new StringBuilder("Thanhtoandonhang").append(generateCustomId());
        StringBuilder qr = new StringBuilder("https://qr.sepay.vn/img?acc=");
        qr.append(sepay.getAccount());
        qr.append("&bank=").append(sepay.getBank());
        qr.append("&amount=").append(processQrRequest.getPoint());
        qr.append("&des=").append(desc);
        qr.append("&template=TEMPLATE&download=DOWNLOAD");
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .status(ETransaction.PENDING)
                .account(getAuthenticatedAccount())
                .amount(BigDecimal.valueOf(processQrRequest.getPoint()))
                .transactionDescription(desc.toString())
                .minDate(LocalDateTime.now())
                .maxDate(LocalDateTime.now().plusMinutes(5))
                .build();
        TransactionEntity transaction = transactionDAO.saveTransaction(transactionEntity);
        return ProcessQrResponse.builder()
                .qr(qr.toString())
                .tranId(transaction.getId())
                .build();
    }

    @Override
    public WalletResponse getWallet() {
        AccountEntity accountCurrent = getAuthenticatedAccount();
        WalletEntity wallet = walletDAO.getWallet(accountCurrent.getId());
        return WalletResponse.builder()
                .balance(wallet.getBalance())
                .build();
    }

    @Override
    public void authenticateTransactional(WebhooksRequest webhooksRequest) {
        String desc = webhooksRequest.getDescription().split("\\s+")[1];
        TransactionEntity transaction = transactionDAO.findByTransactionDescription(desc);
        if (transaction == null) {
            throw new ForbiddenException("Không tìm thấy nội dung giao dịch tương ứng");
        }
        AccountEntity account = transaction.getAccount();
        WalletEntity wallet = walletDAO.getWallet(account.getId());
        if (wallet == null) {
            throw new RuntimeException("Lỗi hệ thống");
        }
        wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
        walletDAO.updateWallet(wallet);
        transaction.setStatus(ETransaction.SUCCESS);
        transactionDAO.update(transaction);
        PaymentResultWs.sendToUser(transaction.getId(), WsResponse.builder()
                .status(HttpServletResponse.SC_OK)
                .message("Xác nhận giao dịch thành công")
                .build());
    }

    @Override
    public CheckTransaction checkTransaction(Transaction transaction) {
        String desc = transaction.getTransactionContent().split("\\s+")[1];
        TransactionEntity transactionEntity = transactionDAO.findByTransactionDescription(desc);
        if (transactionEntity == null) {
            return CheckTransaction.builder().success(false).build();
        }
        AccountEntity account = transactionEntity.getAccount();
        WalletEntity wallet = walletDAO.getWallet(account.getId());
        if (wallet == null) {
            throw new RuntimeException("Lỗi hệ thống");
        }
        wallet.setBalance(wallet.getBalance().add(transactionEntity.getAmount()));
        walletDAO.updateWallet(wallet);
        transactionEntity.setStatus(ETransaction.SUCCESS);
        transactionDAO.update(transactionEntity);
        return CheckTransaction.builder().success(true).build();
    }

    private AccountEntity getAuthenticatedAccount() {
        String email = AuthenticationContextHolder.getContext().getEmail();
        return accountDAO.findByEmail(email);
    }

}
