package com.boauto.backoffice.admin.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@EnabledIfEnvironmentVariable(named = "RUN_DB_INTEGRATION_TEST", matches = "true")
class MemberInfoMyBatisIntegrationTest {

    @Autowired
    private MemberInfoQueryRepository memberInfoQueryRepository;

    @Test
    void 검색조건별_카운트와_목록이_정상_조회된다() {
        MemberInfoSearchCriteria allCriteria = new MemberInfoSearchCriteria(
                "", "", "PHONE", "", "", "ALL", "ALL", "ALL", 1, 10
        );

        int allCount = memberInfoQueryRepository.count(allCriteria);
        List<MemberInfoRow> allRows = memberInfoQueryRepository.find(allCriteria, 0, 10);

        assertThat(allCount).isGreaterThan(0);
        assertThat(allRows).isNotEmpty();

        MemberInfoSearchCriteria keywordCriteria = new MemberInfoSearchCriteria(
                "", "", "NAME", "홍", "", "ALL", "ALL", "ALL", 1, 10
        );

        int keywordCount = memberInfoQueryRepository.count(keywordCriteria);
        List<MemberInfoRow> keywordRows = memberInfoQueryRepository.find(keywordCriteria, 0, 10);

        assertThat(keywordCount).isGreaterThan(0);
        assertThat(keywordRows).allMatch(row -> row.name().contains("홍"));
    }
}
