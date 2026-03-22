package com.boauto.backoffice.admin.member;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberInfoServiceTest {

    @Test
    void 첫_페이지_페이징과_표시_변환을_처리한다() {
        InMemoryRepository repository = new InMemoryRepository(sampleRows());
        MemberInfoService service = new MemberInfoService(repository);

        MemberInfoSearchCriteria criteria = new MemberInfoSearchCriteria(
                "", "", "PHONE", "", "", "ALL", "ALL", "ALL", 1, 10
        );

        MemberInfoSearchResult result = service.search(criteria);

        assertThat(result.totalCount()).isEqualTo(12);
        assertThat(result.filteredCount()).isEqualTo(12);
        assertThat(result.currentPage()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.members()).hasSize(10);
        assertThat(result.members().get(0).phone()).contains("****");
        assertThat(result.members().get(0).memberStatus()).isIn("정회원", "준회원", "탈퇴오류");
    }

    @Test
    void 마지막_페이지를_초과한_요청은_마지막_페이지로_보정된다() {
        InMemoryRepository repository = new InMemoryRepository(sampleRows());
        MemberInfoService service = new MemberInfoService(repository);

        MemberInfoSearchCriteria criteria = new MemberInfoSearchCriteria(
                "", "", "PHONE", "", "", "ALL", "ALL", "ALL", 999, 10
        );

        MemberInfoSearchResult result = service.search(criteria);

        assertThat(result.currentPage()).isEqualTo(2);
        assertThat(result.members()).hasSize(2);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    void 검색결과가_없으면_빈_목록을_반환한다() {
        InMemoryRepository repository = new InMemoryRepository(sampleRows());
        MemberInfoService service = new MemberInfoService(repository);

        MemberInfoSearchCriteria criteria = new MemberInfoSearchCriteria(
                "", "", "NAME", "없는이름", "", "ALL", "ALL", "ALL", 1, 10
        );

        MemberInfoSearchResult result = service.search(criteria);

        assertThat(result.filteredCount()).isZero();
        assertThat(result.members()).isEmpty();
        assertThat(result.pageStart()).isZero();
        assertThat(result.pageEnd()).isZero();
    }

    private List<MemberInfoRow> sampleRows() {
        List<MemberInfoRow> rows = new ArrayList<>();
        String[] names = {"홍*동", "이*영", "김*수", "박*민", "최*정", "한*림"};
        String[] genders = {"M", "F"};
        String[] statuses = {"REGULAR", "PRE_REGULAR", "WITHDRAW_ERROR"};
        String[] restrictions = {"NORMAL", "ADMIN_LIMITED"};
        String[] channels = {"라운드", "휘슬", "네셀카드", "이벤트"};

        for (int i = 0; i < 12; i++) {
            LocalDateTime joinedAt = LocalDateTime.of(2025, 1, 1, 9, 0)
                    .plusDays(i)
                    .plusMinutes((i * 7L) % 60);
            LocalDateTime convertedAt = (i % 3 == 0) ? joinedAt.plusHours(2) : null;

            rows.add(new MemberInfoRow(
                    joinedAt,
                    names[i % names.length],
                    "010-" + String.format("%04d", 1100 + i) + "-" + String.format("%04d", 2100 + i),
                    "RRP" + (20250314000000L + i),
                    String.format("%02d****", 80 + (i % 20)),
                    genders[i % genders.length],
                    statuses[i % statuses.length],
                    convertedAt,
                    restrictions[i % restrictions.length],
                    channels[i % channels.length]
            ));
        }
        return rows;
    }

    private static class InMemoryRepository implements MemberInfoQueryRepository {
        private final List<MemberInfoRow> rows;

        private InMemoryRepository(List<MemberInfoRow> rows) {
            this.rows = rows;
        }

        @Override
        public int count(MemberInfoSearchCriteria criteria) {
            return filtered(criteria).size();
        }

        @Override
        public List<MemberInfoRow> find(MemberInfoSearchCriteria criteria, int offset, int size) {
            List<MemberInfoRow> filtered = filtered(criteria);
            int fromIndex = Math.min(offset, filtered.size());
            int toIndex = Math.min(fromIndex + size, filtered.size());
            return filtered.subList(fromIndex, toIndex);
        }

        private List<MemberInfoRow> filtered(MemberInfoSearchCriteria criteria) {
            return rows.stream()
                    .filter(row -> matchesKeyword(row, criteria))
                    .sorted(Comparator.comparing(MemberInfoRow::joinedAt).reversed())
                    .toList();
        }

        private boolean matchesKeyword(MemberInfoRow row, MemberInfoSearchCriteria criteria) {
            if (criteria.getKeyword().isBlank()) {
                return true;
            }
            if ("NAME".equals(criteria.getSearchType())) {
                return row.name().contains(criteria.getKeyword());
            }
            return true;
        }
    }
}
