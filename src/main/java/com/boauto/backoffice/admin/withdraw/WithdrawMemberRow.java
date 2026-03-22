package com.boauto.backoffice.admin.withdraw;

import java.time.LocalDateTime;

public record WithdrawMemberRow(
        LocalDateTime withdrewAt,
        String name,
        String phone,
        String customerId,
        String inflowChannel,
        String manualMemo
) {
}
