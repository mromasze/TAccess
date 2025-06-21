package pl.mromasze.taccess.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.mromasze.taccess.bot.entity.product.Product;
import pl.mromasze.taccess.web.services.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ProductService productService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("logout", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "dashboard";
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}