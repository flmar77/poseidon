package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;

@Slf4j
@Controller
public class VisitorController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        log.debug("get home");
        return "/home";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("get login");
        return "/login";
    }

    @GetMapping("/login-error")
    public String loginError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginError", true);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginLogout", true);
        return "redirect:/login";
    }

    @GetMapping("/create-account")
    public String getCreateAccount(Model model) {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole("USER");
        model.addAttribute("userEntity", userEntity);
        return "/create-account";
    }

    @PostMapping("/create-account")
    public String postCreateAccount(@Valid @ModelAttribute UserEntity userEntity,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if (result.hasErrors()) {
            return "create-account";
        }
        try {
            UserEntity userEntitySaved = userService.createUser(userEntity);
            log.debug("user created with id : " + userEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedUser", true);
            return "redirect:/user/home";
        } catch (EntityExistsException e) {
            log.debug("user not created because username already exists : " + userEntity.getUserName());
            model.addAttribute("wrongCreatedUser", true);
            return "/create-account";
        }
    }

}
