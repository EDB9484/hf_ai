package com.boauto.backoffice.admin.member;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Service
public class MemberInfoService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<MemberRow> sampleRows = createSampleRows();

    public MemberInfoSearchResult search(MemberInfoSearchCriteria criteria) {
        List<MemberRow> filtered = sampleRows.stream()
                .filter(row -> matchesDateRange(row, criteria))
                .filter(row -> matchesKeyword(row, criteria))
                .filter(row -> matchesBirthDate(row, criteria))
                .filter(row -> matchesSimple(row.gender, criteria.getGender()))
                .filter(row -> matchesSimple(row.memberStatus, criteria.getMemberStatus()))
                .filter(row -> matchesSimple(row.restriction, criteria.getRestriction()))
                .sorted(Comparator.comparing((MemberRow row) -> row.joinedAt).reversed())
                .toList();

        int totalCount = sampleRows.size();
        int filteredCount = filtered.size();
        int totalPages = Math.max(1, (int) Math.ceil(filteredCount / (double) criteria.getSize()));
        int currentPage = Math.min(Math.max(criteria.getPage(), 1), totalPages);

        int fromIndex = Math.min((currentPage - 1) * criteria.getSize(), filteredCount);
        int toIndex = Math.min(fromIndex + criteria.getSize(), filteredCount);

        List<MemberInfoView> members = filtered.subList(fromIndex, toIndex).stream()
                .map(MemberRow::toView)
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

    private boolean matchesDateRange(MemberRow row, MemberInfoSearchCriteria criteria) {
        LocalDate from = parseDate(criteria.getJoinedFrom());
        LocalDate to = parseDate(criteria.getJoinedTo());

        LocalDate joinedDate = row.joinedAt.toLocalDate();
        if (from != null && joinedDate.isBefore(from)) {
            return false;
        }
        return to == null || !joinedDate.isAfter(to);
    }

    private boolean matchesKeyword(MemberRow row, MemberInfoSearchCriteria criteria) {
        if (criteria.getKeyword().isBlank()) {
            return true;
        }

        String keyword = criteria.getKeyword().toLowerCase(Locale.ROOT);
        return switch (criteria.getSearchType()) {
            case "NAME" -> row.name.toLowerCase(Locale.ROOT).contains(keyword);
            case "CUSTOMER_ID" -> row.customerId.toLowerCase(Locale.ROOT).contains(keyword);
            case "PHONE" -> row.phone.replace("-", "").contains(keyword.replace("-", ""));
            default -> true;
        };
    }

    private boolean matchesBirthDate(MemberRow row, MemberInfoSearchCriteria criteria) {
        if (criteria.getBirthDate().isBlank()) {
            return true;
        }
        return row.birthDate.startsWith(criteria.getBirthDate());
    }

    private boolean matchesSimple(String value, String criterion) {
        return "ALL".equals(criterion) || value.equals(criterion);
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private List<MemberRow> createSampleRows() {
        List<MemberRow> rows = new ArrayList<>();
        String[] names = {"홍*동", "이*영", "김*수", "박*민", "최*정", "한*림"};
        String[] genders = {"M", "F"};
        String[] statuses = {"REGULAR", "PRE_REGULAR", "WITHDRAW_ERROR"};
        String[] restrictions = {"NORMAL", "ADMIN_LIMITED"};
        String[] channels = {"라운드", "휘슬", "네셀카드", "이벤트"};

        for (int i = 0; i < 72; i++) {
            LocalDateTime joinedAt = LocalDateTime.of(2025, 1, 1, 9, 0)
                    .plusDays(i)
                    .plusMinutes((i * 7L) % 60);
            LocalDateTime convertedAt = (i % 3 == 0) ? joinedAt.plusHours(2) : null;

            String phone = "010-" + String.format("%04d", (1100 + i)) + "-" + String.format("%04d", (2100 + i));
            String customerId = "RRP" + (20250314000000L + i);
            String birthDate = String.format("%02d****", (80 + (i % 20)));

            rows.add(new MemberRow(
                    joinedAt,
                    names[i % names.length],
                    phone,
                    customerId,
                    birthDate,
                    genders[i % genders.length],
                    statuses[i % statuses.length],
                    convertedAt,
                    restrictions[i % restrictions.length],
                    channels[i % channels.length]
            ));
        }

        return rows;
    }

    private static class MemberRow {
        private final LocalDateTime joinedAt;
        private final String name;
        private final String phone;
        private final String customerId;
        private final String birthDate;
        private final String gender;
        private final String memberStatus;
        private final LocalDateTime convertedAt;
        private final String restriction;
        private final String inflowChannel;

        private MemberRow(LocalDateTime joinedAt, String name, String phone, String customerId,
                          String birthDate, String gender, String memberStatus, LocalDateTime convertedAt,
                          String restriction, String inflowChannel) {
            this.joinedAt = joinedAt;
            this.name = name;
            this.phone = phone;
            this.customerId = customerId;
            this.birthDate = birthDate;
            this.gender = gender;
            this.memberStatus = memberStatus;
            this.convertedAt = convertedAt;
            this.restriction = restriction;
            this.inflowChannel = inflowChannel;
        }

        private MemberInfoView toView() {
            return new MemberInfoView(
                    joinedAt.format(DATE_TIME_FORMATTER),
                    name,
                    maskPhone(phone),
                    customerId,
                    birthDate,
                    "M".equals(gender) ? "남" : "여",
                    toStatusLabel(memberStatus),
                    convertedAt == null ? "-" : convertedAt.format(DATE_TIME_FORMATTER),
                    toRestrictionLabel(restriction),
                    inflowChannel
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
}
