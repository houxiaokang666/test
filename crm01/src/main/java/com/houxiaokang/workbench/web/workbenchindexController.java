package com.houxiaokang.workbench.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class workbenchindexController {
    @RequestMapping("/workbench/index.do")
    public String toindex() {
        return "workbench/index";
    }
}
