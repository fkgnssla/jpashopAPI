package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/new")
    public String createForm(@ModelAttribute MemberForm memberForm) {
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute MemberForm memberForm, BindingResult result) {

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(new Address(memberForm.getCity(), memberForm.getStreet(),memberForm.getZipcode()));

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }
}
