package com.boauto.backoffice.admin.withdraw;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Service
public class WithdrawMemberService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<WithdrawMemberRow> sampleRows;

    public WithdrawMemberService() {
        this.sampleRows = createSampleRows();
    }

    public WithdrawMemberSearchResult search(WithdrawMemberSearchCriteria criteria) {
        DateRange dateRange = resolveDateRange(criteria);

        List<WithdrawMemberRow> filteredRows = sampleRows.stream()
                .filter(row -> isInDateRange(row.withdrewAt(), dateRange))
                .filter(row -> matchesKeyword(row, criteria))
                .sorted(Comparator.comparing(WithdrawMemberRow::withdrewAt).reversed())
                .toList();

        int totalCount = sampleRows.size();
        int filteredCount = filteredRows.size();

        int totalPages = Math.max(1, (int) Math.ceil(filteredCount / (double) criteria.getSize()));
        int currentPage = Math.min(Math.max(criteria.getPage(), 1), totalPages);

        int fromIndex = Math.min((currentPage - 1) * criteria.getSize(), filteredCount);
        int toIndex = Math.min(fromIndex + criteria.getSize(), filteredCount);

        List<WithdrawMemberView> members = filteredRows.subList(fromIndex, toIndex)
                .stream()
                .map(this::toView)
                .toList();

        int pageStart = filteredCount == 0 ? 0 : fromIndex + 1;
        int pageEnd = toIndex;

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);
        List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage).boxed().toList();

        return new WithdrawMemberSearchResult(
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

    private DateRange resolveDateRange(WithdrawMemberSearchCriteria criteria) {
        LocalDate today = LocalDate.now();
        LocalDate fromDate = parseDate(criteria.getWithdrewFrom(), today);
        LocalDate toDate = parseDate(criteria.getWithdrewTo(), fromDate);

        if (toDate.isBefore(fromDate)) {
            toDate = fromDate;
        }

        return new DateRange(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
    }

    private LocalDate parseDate(String value, LocalDate defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(value);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    private boolean isInDateRange(LocalDateTime withdrewAt, DateRange range) {
        return !withdrewAt.isBefore(range.start()) && !withdrewAt.isAfter(range.end());
    }

    private boolean matchesKeyword(WithdrawMemberRow row, WithdrawMemberSearchCriteria criteria) {
        if (criteria.getKeyword().isBlank()) {
            return true;
        }

        String keyword = criteria.getKeyword().trim();
        if ("CUSTOMER_ID".equals(criteria.getSearchType())) {
            return row.customerId().equalsIgnoreCase(keyword);
        }

        String normalizedKeyword = keyword.replaceAll("[^0-9]", "");
        String normalizedPhone = row.phone().replaceAll("[^0-9]", "");
        return normalizedPhone.equals(normalizedKeyword);
    }

    private WithdrawMemberView toView(WithdrawMemberRow row) {
        return new WithdrawMemberView(
                row.withdrewAt().format(DATE_TIME_FORMATTER),
                maskName(row.name()),
                maskPhone(row.phone()),
                row.customerId(),
                row.inflowChannel(),
                row.manualMemo().isBlank() ? "-" : row.manualMemo()
        );
    }

    private String maskName(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        if (name.length() == 1) {
            return "*";
        }
        return name.charAt(0) + "*" + name.substring(name.length() - 1);
    }

    private String maskPhone(String source) {
        String[] parts = source.split("-");
        if (parts.length != 3) {
            return source;
        }
        return parts[0] + "-****-" + parts[2];
    }

    private List<WithdrawMemberRow> createSampleRows() {
        LocalDateTime base = LocalDateTime.now().withHour(15).withMinute(30).withSecond(0).withNano(0);
        List<String> channels = List.of("라운드", "휘슬", "더쎈카드");

        return IntStream.range(0, 57)
                .mapToObj(index -> {
                    LocalDateTime withdrewAt = base.minusHours(index * 3L);
                    String name = switch (index % 6) {
                        case 0 -> "홍동현";
                        case 1 -> "김민지";
                        case 2 -> "박지훈";
                        case 3 -> "이서연";
                        case 4 -> "최지우";
                        default -> "정하늘";
                    };
                    String phone = String.format(Locale.ROOT, "010-%04d-%04d", 1000 + index, 1200 + index);
                    String customerId = "RRP" + String.format(Locale.ROOT, "%011d", 2025031400000L + index);
                    String channel = channels.get(index % channels.size());
                    String memo = (index % 5 == 0) ? "haeuny" : "";
                    return new WithdrawMemberRow(withdrewAt, name, phone, customerId, channel, memo);
                })
                .toList();
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {
    }
}
