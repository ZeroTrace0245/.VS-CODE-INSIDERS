package com.assignment.greeting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GreetingController {

    private String storedName = "";

    @GetMapping("/")
    public String home(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "index";
    }

    @PostMapping("/submit")
    public String submitName(@RequestParam(value = "name", required = false) String name,
                             RedirectAttributes redirectAttributes) {
        String safeName = name == null ? "" : name.trim();

        if (safeName.isEmpty()) {
            redirectAttributes.addAttribute("error", "Please enter your name before continuing.");
            return "redirect:/";
        }

        storedName = safeName;
        redirectAttributes.addAttribute("name", storedName);
        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public String welcome(@RequestParam(value = "name", required = false) String name, Model model) {
        String displayName = (name == null || name.trim().isEmpty()) ? storedName : name.trim();
        model.addAttribute("name", displayName);
        return "welcome";
    }
}
