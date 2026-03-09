package com.boauto.backoffice.admin;

import com.boauto.backoffice.admin.member.MemberInfoSearchCriteria;
import com.boauto.backoffice.admin.member.MemberInfoSearchResult;
import com.boauto.backoffice.admin.member.MemberInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminHomeController {

    private final MemberInfoService memberInfoService;

    public AdminHomeController(MemberInfoService memberInfoService) {
        this.memberInfoService = memberInfoService;
    }

    @GetMapping({"/", "/admin"})
    public String index() {
        return "admin/index";
    }

    @GetMapping("/admin/ia/member-info")
    public String memberInfo(
            @RequestParam(defaultValue = "") String joinedFrom,
            @RequestParam(defaultValue = "") String joinedTo,
            @RequestParam(defaultValue = "PHONE") String searchType,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String birthDate,
            @RequestParam(defaultValue = "ALL") String gender,
            @RequestParam(defaultValue = "ALL") String memberStatus,
            @RequestParam(defaultValue = "ALL") String restriction,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        MemberInfoSearchCriteria criteria = new MemberInfoSearchCriteria(
                joinedFrom, joinedTo, searchType, keyword, birthDate, gender, memberStatus, restriction, page, size
        );
        MemberInfoSearchResult result = memberInfoService.search(criteria);

        model.addAttribute("criteria", criteria);
        model.addAttribute("result", result);
        return "admin/member-info";
    }
}
