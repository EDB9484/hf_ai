package com.boauto.backoffice.admin.withdraw;

import java.util.List;

public record WithdrawMemberSearchResult(
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
        List<WithdrawMemberView> members
) {
}
