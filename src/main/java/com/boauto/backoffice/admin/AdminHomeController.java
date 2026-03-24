package com.boauto.backoffice.admin;

import com.boauto.backoffice.admin.member.MemberInfoSearchCriteria;
import com.boauto.backoffice.admin.member.MemberInfoSearchResult;
import com.boauto.backoffice.admin.member.MemberInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

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
            @RequestParam(defaultValue = "") String preset,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        LocalDate now = LocalDate.now();
        String defaultDate = now.toString();
        String normalizedFrom = joinedFrom == null ? "" : joinedFrom.trim();
        String normalizedTo = joinedTo == null ? "" : joinedTo.trim();

        if (normalizedFrom.isBlank()) {
            normalizedFrom = defaultDate;
        }
        if (normalizedTo.isBlank()) {
            normalizedTo = defaultDate;
        }

        if (!preset.isBlank()) {
            switch (preset) {
                case "TODAY" -> {
                    normalizedFrom = now.toString();
                    normalizedTo = now.toString();
                }
                case "WEEK" -> {
                    normalizedFrom = now.minusDays(6).toString();
                    normalizedTo = now.toString();
                }
                case "MONTH" -> {
                    normalizedFrom = now.minusMonths(1).toString();
                    normalizedTo = now.toString();
                }
                case "ALL" -> {
                    normalizedFrom = "2000-01-01";
                    normalizedTo = "2999-12-31";
                }
                default -> {
                    // preset 값이 유효하지 않으면 사용자가 입력한 일자 범위를 유지한다.
                }
            }
        }

        MemberInfoSearchCriteria criteria = new MemberInfoSearchCriteria(
                normalizedFrom, normalizedTo, searchType, keyword, birthDate, gender, memberStatus, restriction, page, size
        );
        MemberInfoSearchResult result = memberInfoService.search(criteria);

        model.addAttribute("criteria", criteria);
        model.addAttribute("result", result);
        return "admin/member-info";
    }
}
