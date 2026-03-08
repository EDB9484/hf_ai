package com.boauto.backoffice.admin.member;

public record MemberInfoView(
        String joinedAt,
        String name,
        String phone,
        String customerId,
        String birthDate,
        String gender,
        String memberStatus,
        String convertedAt,
        String restriction,
        String inflowChannel
) {
}
