package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/home")
    public String getUserHome(Authentication authentication,
                              Model model) {
        log.debug("get /user/home as : " + authentication.getName());
        model.addAttribute("username", authentication.getName());
        return "/user/home";
    }

    @GetMapping("/user/list")
    public String getUserList(Model model) {
        log.debug("get all users");
        model.addAttribute("userEntities", userService.getAllUsers());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String getUserAdd(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "user/add";
    }

    @PostMapping("/user/add")
    public String postUserAdd(@Valid @ModelAttribute UserEntity userEntity,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            return "/user/add";
        }
        try {
            UserEntity userEntitySaved = userService.createUser(userEntity);
            log.debug("user created with id : " + userEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedUser", true);
            return "redirect:/user/home";
        } catch (EntityExistsException e) {
            log.debug("user not created because username already exists : " + userEntity.getUserName());
            model.addAttribute("wrongCreatedUser", true);
            return "/user/add";
        }
    }

    @GetMapping("/user/update/{id}")
    public String getUserUpdate(@PathVariable("id") Integer id,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            UserEntity userEntity = userService.getUserById(id);
            model.addAttribute("userEntity", userEntity);
            return "user/update";
        } catch (NoSuchElementException e) {
            log.debug("can't update missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
            return "redirect:/user/list";
        }
    }

    @PostMapping("/user/update/{id}")
    public String postUserUpdate(@PathVariable("id") Integer id,
                                 @Valid UserEntity userEntity,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/update";
        }

        try {
            userEntity.setId(id);
            UserEntity userEntitySaved = userService.updateUser(userEntity);
            log.debug("user updated with id : " + userEntitySaved.getId());
            model.addAttribute("rightUpdatedUser", true);
            return "/user/update";
        } catch (NoSuchElementException e) {
            log.debug("can't update missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
            return "redirect:/user/list";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(id);
            log.debug("user deleted with id : " + id);
            redirectAttributes.addFlashAttribute("rightDeletedUser", true);
            return "redirect:/user/list";
        } catch (NoSuchElementException e) {
            log.debug("can't delete missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
            return "redirect:/user/list";
        }
    }
}
