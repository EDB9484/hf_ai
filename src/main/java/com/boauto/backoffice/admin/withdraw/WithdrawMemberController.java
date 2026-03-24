package com.boauto.backoffice.admin.withdraw;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class WithdrawMemberController {

    private final WithdrawMemberService withdrawMemberService;

    public WithdrawMemberController(WithdrawMemberService withdrawMemberService) {
        this.withdrawMemberService = withdrawMemberService;
    }

    @GetMapping("/admin/ia/withdraw-members")
    public String withdrawMembers(
            @RequestParam(defaultValue = "") String withdrewFrom,
            @RequestParam(defaultValue = "") String withdrewTo,
            @RequestParam(defaultValue = "PHONE") String searchType,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String preset,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        String defaultDate = LocalDate.now().toString();
        LocalDate now = LocalDate.now();

        String normalizedFrom = withdrewFrom == null || withdrewFrom.isBlank() ? defaultDate : withdrewFrom;
        String normalizedTo = withdrewTo == null || withdrewTo.isBlank() ? defaultDate : withdrewTo;

        if (!preset.isBlank()) {
            switch (preset) {
                case "TODAY" -> {
                    normalizedFrom = now.toString();
                    normalizedTo = now.toString();
                }
                case "YESTERDAY" -> {
                    normalizedFrom = now.minusDays(1).toString();
                    normalizedTo = now.minusDays(1).toString();
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

        WithdrawMemberSearchCriteria criteria = new WithdrawMemberSearchCriteria(
                normalizedFrom,
                normalizedTo,
                searchType,
                keyword,
                page,
                size
        );

        WithdrawMemberSearchResult result = withdrawMemberService.search(criteria);

        model.addAttribute("criteria", criteria);
        model.addAttribute("result", result);
        return "admin/withdraw-members";
    }
}
