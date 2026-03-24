package com.boauto.backoffice.admin.member;

import java.util.List;

public interface MemberInfoQueryRepository {

    int count(MemberInfoSearchCriteria criteria);

    List<MemberInfoRow> find(MemberInfoSearchCriteria criteria, int offset, int size);
}
