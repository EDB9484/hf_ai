package com.boauto.backoffice.admin.member;

public class MemberInfoSearchCriteria {

    private String joinedFrom;
    private String joinedTo;
    private String searchType;
    private String keyword;
    private String birthDate;
    private String gender;
    private String memberStatus;
    private String restriction;
    private int page;
    private int size;

    public MemberInfoSearchCriteria(String joinedFrom, String joinedTo, String searchType, String keyword,
                                    String birthDate, String gender, String memberStatus, String restriction,
                                    int page, int size) {
        this.joinedFrom = normalize(joinedFrom);
        this.joinedTo = normalize(joinedTo);
        this.searchType = normalize(searchType).isEmpty() ? "PHONE" : searchType;
        this.keyword = normalize(keyword);
        this.birthDate = normalize(birthDate);
        this.gender = normalize(gender).isEmpty() ? "ALL" : gender;
        this.memberStatus = normalize(memberStatus).isEmpty() ? "ALL" : memberStatus;
        this.restriction = normalize(restriction).isEmpty() ? "ALL" : restriction;
        this.page = Math.max(page, 1);
        this.size = (size == 20 || size == 50) ? size : 10;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public String getJoinedFrom() {
        return joinedFrom;
    }

    public String getJoinedTo() {
        return joinedTo;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public String getRestriction() {
        return restriction;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public MemberInfoSearchCriteria withoutFilters() {
        return new MemberInfoSearchCriteria("", "", "PHONE", "", "", "ALL", "ALL", "ALL", 1, size);
    }
}
