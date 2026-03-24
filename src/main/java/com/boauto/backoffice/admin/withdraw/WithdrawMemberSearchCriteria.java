package com.boauto.backoffice.admin.withdraw;

public class WithdrawMemberSearchCriteria {

    private final String withdrewFrom;
    private final String withdrewTo;
    private final String searchType;
    private final String keyword;
    private final int page;
    private final int size;

    public WithdrawMemberSearchCriteria(String withdrewFrom, String withdrewTo, String searchType,
                                        String keyword, int page, int size) {
        this.withdrewFrom = normalize(withdrewFrom);
        this.withdrewTo = normalize(withdrewTo);
        String normalizedSearchType = normalize(searchType);
        this.searchType = normalizedSearchType.isEmpty() ? "PHONE" : normalizedSearchType;
        this.keyword = normalize(keyword);
        this.page = Math.max(page, 1);
        this.size = (size == 25 || size == 50 || size == 100) ? size : 10;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public String getWithdrewFrom() {
        return withdrewFrom;
    }

    public String getWithdrewTo() {
        return withdrewTo;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
