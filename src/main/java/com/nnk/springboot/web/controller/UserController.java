package com.nnk.springboot.web.controller;

import com.nnk.springboot.dal.entity.UserEntity;
import com.nnk.springboot.dal.repository.UserRepository;
import com.nnk.springboot.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

@Slf4j
@Controller
public class UserController {

    // TODO : delete
    @Autowired
    private UserRepository userRepository;

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
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userEntity.setPassword("");
        model.addAttribute("user", userEntity);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid UserEntity userEntity,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setId(id);
        userRepository.save(userEntity);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(userEntity);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }
}
