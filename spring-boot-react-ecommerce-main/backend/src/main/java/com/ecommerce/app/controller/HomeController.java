package com.ecommerce.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {
        "/",
        "/{path:[^\\.]*}",
        "/{path:[^\\.]*}/{path2:[^\\.]*}"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
