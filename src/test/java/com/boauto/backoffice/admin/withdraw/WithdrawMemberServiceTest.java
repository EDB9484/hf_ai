package com.boauto.backoffice.admin.withdraw;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WithdrawMemberServiceTest {

    private final WithdrawMemberService withdrawMemberService = new WithdrawMemberService();

    @Test
    void search_returnsMaskedDataAndPagination() {
        WithdrawMemberSearchCriteria criteria = new WithdrawMemberSearchCriteria(
                "2026-03-01",
                "2026-03-31",
                "PHONE",
                "",
                1,
                10
        );

        WithdrawMemberSearchResult result = withdrawMemberService.search(criteria);

        assertThat(result.totalCount()).isGreaterThan(0);
        assertThat(result.filteredCount()).isGreaterThan(0);
        assertThat(result.members()).hasSizeLessThanOrEqualTo(10);
        assertThat(result.members().get(0).name()).contains("*");
        assertThat(result.members().get(0).phone()).contains("****");
    }

    @Test
    void search_filtersByCustomerId() {
        WithdrawMemberSearchCriteria unfiltered = new WithdrawMemberSearchCriteria(
                "2025-01-01",
                "2026-12-31",
                "PHONE",
                "",
                1,
                10
        );
        WithdrawMemberSearchResult baseResult = withdrawMemberService.search(unfiltered);

        String customerId = baseResult.members().get(0).customerId();

        WithdrawMemberSearchCriteria criteria = new WithdrawMemberSearchCriteria(
                "2025-01-01",
                "2026-12-31",
                "CUSTOMER_ID",
                customerId,
                1,
                10
        );

        WithdrawMemberSearchResult result = withdrawMemberService.search(criteria);

        assertThat(result.filteredCount()).isEqualTo(1);
        assertThat(result.members()).hasSize(1);
        assertThat(result.members().get(0).customerId()).isEqualTo(customerId);
    }
}
