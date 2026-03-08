package com.boauto.backoffice.admin.member;

import java.util.List;

public record MemberInfoSearchResult(
        int totalCount,
        int filteredCount,
        int currentPage,
        int pageSize,
        int totalPages,
        int pageStart,
        int pageEnd,
        boolean hasPrev,
        boolean hasNext,
        List<Integer> pageNumbers,
        List<MemberInfoView> members
) {
}
