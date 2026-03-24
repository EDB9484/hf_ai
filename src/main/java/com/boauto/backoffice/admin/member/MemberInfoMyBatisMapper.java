package com.boauto.backoffice.admin.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberInfoMyBatisMapper extends MemberInfoQueryRepository {

    @Override
    int count(@Param("criteria") MemberInfoSearchCriteria criteria);

    @Override
    List<MemberInfoRow> find(
            @Param("criteria") MemberInfoSearchCriteria criteria,
            @Param("offset") int offset,
            @Param("size") int size
    );
}
