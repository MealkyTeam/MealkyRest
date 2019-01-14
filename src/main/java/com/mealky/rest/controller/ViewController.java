package com.mealky.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
    @GetMapping("/form/setpassword")
    public String setPassword(@RequestParam(name = "token", required = false) String token, Model model) {
        if (token == null) return "resetpassword";
        model.addAttribute("token", token);
        return "setpassword";
    }

    @GetMapping("/form/changepassword")
    public String changePassword() {
        return "changepassword";
    }

    @GetMapping("/form/resetpassword")
    public String resetPassword() {
        return "resetpassword";
    }
}
