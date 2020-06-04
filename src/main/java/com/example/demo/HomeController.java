package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/")
    public String index(Principal principal, Model model) {
        String userName = principal.getName();
        model.addAttribute("username", userRepository.findByUsername(userName));
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }

    @RequestMapping("/admin")
    public String admin(Principal principal, Model model) {
        String userName = principal.getName();
        model.addAttribute("username", userRepository.findByUsername(userName));
        return "admin";
    }

    @RequestMapping("/secure")
    public String detailInformation(Principal principal, Model model){
        String userName = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(userName));
        model.addAttribute("roles", roleRepository.findAllByUsername(userName));
        return "secure";
    }

    @RequestMapping("/register")
    public String registrationPage(Principal principal,Model model){
        String userName = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(userName));
        model.addAttribute("newUser", new User());
        return "registrationForm";
    }

    @PostMapping("/processForm")
    public String processForm(@Valid @ModelAttribute("newUser") User user, BindingResult result, Model model){
        if(result.hasErrors()){
            user.clearPassword();
            model.addAttribute("newUser", user);
            return "registrationForm";
        }
        else{
            Role role = new Role(user.getUsername(), "ROLE_USER");
            user.setEnabled(true);
            userRepository.save(user);
            roleRepository.save(role);
            return "successPage";

        }
    }

}
