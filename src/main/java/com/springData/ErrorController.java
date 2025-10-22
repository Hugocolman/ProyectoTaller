package com.springData;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/403")
    public String error403(Model model) {
        model.addAttribute("message", "No tienes permisos para acceder a esta página.");
        return "403"; // Dirige a la plantilla 403.html
    }
}

