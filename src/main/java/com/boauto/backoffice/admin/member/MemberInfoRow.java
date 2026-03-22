package com.boauto.backoffice.admin.member;

import java.time.LocalDateTime;

public record MemberInfoRow(
        LocalDateTime joinedAt,
        String name,
        String phone,
        String customerId,
        String birthDate,
        String gender,
        String memberStatus,
        LocalDateTime convertedAt,
        String restriction,
        String inflowChannel
) {
}
