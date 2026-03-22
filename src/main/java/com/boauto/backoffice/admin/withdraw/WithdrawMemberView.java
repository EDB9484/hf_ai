package com.boauto.backoffice.admin.withdraw;

public record WithdrawMemberView(
        String withdrewAt,
        String name,
        String phone,
        String customerId,
        String inflowChannel,
        String manualMemo
) {
}
