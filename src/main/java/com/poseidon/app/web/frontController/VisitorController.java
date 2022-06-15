package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class VisitorController {

    private static final String authorizationRequestBaseUri = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        log.debug("get home");
        return "/home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        log.debug("get login");

        // TODO : improve ?
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType resolvableType = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (resolvableType != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(resolvableType.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        if (clientRegistrations != null) {
            clientRegistrations.forEach(registration ->
                    oauth2AuthenticationUrls.put(registration.getClientName(),
                            authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
            model.addAttribute("urls", oauth2AuthenticationUrls);
        }

        return "/login";
    }

    @GetMapping("/login-error-account")
    public String loginErrorAccount(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginErrorAccount", true);
        return "redirect:/login";
    }

    @GetMapping("/login-error-oauth2")
    public String loginErrorOauth2(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginErrorOauth2", true);
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
            return "/create-account";
        }
        try {
            UserEntity userEntitySaved = userService.createUser(userEntity);
            log.debug("user created with id : " + userEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedUser", true);
            return "redirect:/user/home";
        } catch (EntityExistsException e) {
            log.error("user not created because username already exists : " + userEntity.getUserName());
            model.addAttribute("wrongCreatedUser", true);
            return "/create-account";
        }
    }

}
