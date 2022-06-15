package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.helper.UserHelper;
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

        String userName = UserHelper.getUserName(authentication);
        log.debug("get /user/home as : " + userName);
        model.addAttribute("username", userName);
        return "/user/home";
    }

    @GetMapping("/user/head")
    public String getUserHead(Authentication authentication, Model model) {
        log.debug("get /user/head as : " + authentication.getName());
        try {
            UserEntity userEntity = userService.getUserByUserName(authentication.getName());
            if (userEntity.getRole().equals("ADMIN")) {
                return "redirect:/user/admin/list";
            }
            return "redirect:/user/user-update/" + userEntity.getId();
        } catch (NoSuchElementException e) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/user-noupdate";
        }

    }

    @GetMapping("/user/user-update/{id}")
    public String getUserUpdate(@PathVariable("id") Integer id,
                                Model model,
                                RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            UserEntity userEntity = userService.getUserById(id);
            model.addAttribute("userEntity", userEntity);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/user-update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
            return "redirect:/user/home";
        }
    }

    @PostMapping("/user/user-update/{id}")
    public String postUserUpdate(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute UserEntity userEntity,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/user-update";
        }

        try {
            userEntity.setId(id);
            UserEntity userEntitySaved = userService.updateUser(userEntity);
            log.debug("user updated with id : " + userEntitySaved.getId());
            model.addAttribute("rightUpdatedUser", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/user-update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
            return "redirect:/user/home";
        }
    }

    // ADMIN PART
    @GetMapping("/user/admin/list")
    public String getUserList(Model model, Authentication authentication) {
        log.debug("get all users");
        model.addAttribute("userEntities", userService.getAllUsers());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/user/admin/list";
    }

    @GetMapping("/user/admin/add")
    public String getUserAdd(Model model, Authentication authentication) {
        model.addAttribute("userEntity", new UserEntity());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/user/admin/add";
    }

    @PostMapping("/user/admin/add")
    public String postUserAdd(@Valid @ModelAttribute UserEntity userEntity,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/admin/add";
        }
        try {
            UserEntity userEntitySaved = userService.createUser(userEntity);
            log.debug("user created with id : " + userEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedUser", true);
            return "redirect:/user/admin/list";
        } catch (EntityExistsException e) {
            log.error("user not created because username already exists : " + userEntity.getUserName());
            model.addAttribute("wrongCreatedUser", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/admin/add";
        }
    }

    @GetMapping("/user/admin/update/{id}")
    public String getUserUpdateAdmin(@PathVariable("id") Integer id,
                                     Model model,
                                     RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            UserEntity userEntity = userService.getUserById(id);
            model.addAttribute("userEntity", userEntity);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/admin/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
            return "redirect:/user/admin/list";
        }
    }

    @PostMapping("/user/admin/update/{id}")
    public String postUserUpdateAdmin(@PathVariable("id") Integer id,
                                      @Valid @ModelAttribute UserEntity userEntity,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/admin/update";
        }

        try {
            userEntity.setId(id);
            UserEntity userEntitySaved = userService.updateUser(userEntity);
            log.debug("user updated with id : " + userEntitySaved.getId());
            model.addAttribute("rightUpdatedUser", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/user/admin/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
            return "redirect:/user/admin/list";
        }
    }

    @GetMapping("/user/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(id);
            log.debug("user deleted with id : " + id);
            redirectAttributes.addFlashAttribute("rightDeletedUser", true);
        } catch (NoSuchElementException e) {
            log.error("can't delete missing user with id : " + id);
            redirectAttributes.addFlashAttribute("missingUserId", true);
        }
        return "redirect:/user/admin/list";
    }

}
