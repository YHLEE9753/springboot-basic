package org.prgrms.kdt.kdtspringorder.voucher.service;

import org.prgrms.kdt.kdtspringorder.VoucherCommandLineApplication;
import org.prgrms.kdt.kdtspringorder.common.enums.VoucherType;
import org.prgrms.kdt.kdtspringorder.voucher.domain.FixedAmountVoucher;
import org.prgrms.kdt.kdtspringorder.voucher.domain.PercentDiscountVoucher;
import org.prgrms.kdt.kdtspringorder.voucher.domain.Voucher;
import org.prgrms.kdt.kdtspringorder.voucher.repository.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

/**
 * 요구사항에 따라 바우처 데이터를 가공하여 반환합니다.
 */
@Service
public class VoucherService {

    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    /**
     * voucherId에 해당하는 바우처 1개를 조회합니다.
     * @param voucherId
     * @return 조회한 바우처를 반환합니다.
     */
    public Voucher getVoucher(UUID voucherId) {
        logger.info("Access getVoucher()");
        logger.info("[Param] voucherId = " + voucherId);
        return this.voucherRepository
                .findById(voucherId)
                .orElseThrow( () -> new RuntimeException(MessageFormat.format("Can not find a voucher for {0}", voucherId)));
    }

    /**
     * 모든 Voucher 목록을 조회합니다.
     * @return 조회한 Voucher 목록을 반환합니다.
     */
    public List<Voucher> getVouchers() {
        logger.info("Access getVouchers()");
        return this.voucherRepository.findAll();
    }

    /**
     * 새 Voucher를 생성하여 등록합니다.
     * @param type 생성할 Voucher 유형
     * @param discount 할인 받을 정도 ( 금액 or % )
     */
    public UUID saveVoucher(VoucherType type, long discount) {

        logger.info("Access saveVoucher()");
        logger.info("[Param] VoucherType = " + type);
        logger.info("[Param] discount = " + discount);

        Voucher createdVoucher = type.createVoucher(discount);
        return this.voucherRepository.insert(createdVoucher);

    }

}
