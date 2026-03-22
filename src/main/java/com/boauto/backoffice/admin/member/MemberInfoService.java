package com.boauto.backoffice.admin.member;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MemberInfoService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final MemberInfoQueryRepository memberInfoQueryRepository;

    public MemberInfoService(MemberInfoQueryRepository memberInfoQueryRepository) {
        this.memberInfoQueryRepository = memberInfoQueryRepository;
    }

    public MemberInfoSearchResult search(MemberInfoSearchCriteria criteria) {
        int totalCount = memberInfoQueryRepository.count(criteria.withoutFilters());
        int filteredCount = memberInfoQueryRepository.count(criteria);

        int totalPages = Math.max(1, (int) Math.ceil(filteredCount / (double) criteria.getSize()));
        int currentPage = Math.min(Math.max(criteria.getPage(), 1), totalPages);

        int fromIndex = Math.min((currentPage - 1) * criteria.getSize(), filteredCount);
        int toIndex = Math.min(fromIndex + criteria.getSize(), filteredCount);

        List<MemberInfoView> members = memberInfoQueryRepository.find(criteria, fromIndex, criteria.getSize())
                .stream()
                .map(this::toView)
                .toList();

        int pageStart = filteredCount == 0 ? 0 : fromIndex + 1;
        int pageEnd = toIndex;

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);
        List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage).boxed().toList();

        return new MemberInfoSearchResult(
                totalCount,
                filteredCount,
                currentPage,
                criteria.getSize(),
                totalPages,
                pageStart,
                pageEnd,
                currentPage > 1,
                currentPage < totalPages,
                pageNumbers,
                members
        );
    }

    private MemberInfoView toView(MemberInfoRow row) {
        return new MemberInfoView(
                row.joinedAt().format(DATE_TIME_FORMATTER),
                row.name(),
                maskPhone(row.phone()),
                row.customerId(),
                row.birthDate(),
                "M".equals(row.gender()) ? "남" : "여",
                toStatusLabel(row.memberStatus()),
                row.convertedAt() == null ? "-" : row.convertedAt().format(DATE_TIME_FORMATTER),
                toRestrictionLabel(row.restriction()),
                row.inflowChannel()
        );
    }

    private String maskPhone(String source) {
        String[] parts = source.split("-");
        if (parts.length != 3) {
            return source;
        }
        return parts[0] + "-****-" + parts[2];
    }

    private String toStatusLabel(String value) {
        return switch (value) {
            case "REGULAR" -> "정회원";
            case "PRE_REGULAR" -> "준회원";
            case "WITHDRAW_ERROR" -> "탈퇴오류";
            default -> value;
        };
    }

    private String toRestrictionLabel(String value) {
        return switch (value) {
            case "NORMAL" -> "정상";
            case "ADMIN_LIMITED" -> "관리자 제한";
            default -> value;
        };
    }
}
