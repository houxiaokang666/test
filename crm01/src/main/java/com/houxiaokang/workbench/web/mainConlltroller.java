package com.houxiaokang.workbench.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class mainConlltroller {
    @RequestMapping("/workbench/main/toindex.do")
    public String toindex() {
        return "workbench/main/index";
    }
}
