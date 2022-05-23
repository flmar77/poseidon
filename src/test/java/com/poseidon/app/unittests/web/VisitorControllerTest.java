package com.poseidon.app.unittests.web;


import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.frontController.VisitorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityExistsException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VisitorController.class)
public class VisitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_returnHome_whenGetRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/home"));
    }

    @Test
    public void should_returnLogin_whenGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"));
    }

    @Test
    public void should_redirectLogin_whenGetLoginErrorAccount() throws Exception {
        mockMvc.perform(get("/login-error-account"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void should_redirectLogin_whenGetLoginErrorOauth2() throws Exception {
        mockMvc.perform(get("/login-error-oauth2"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void should_redirectLogin_whenGetLoginLogout() throws Exception {
        mockMvc.perform(get("/logout")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void should_returnCreateAccount_whenGetCreateAccount() throws Exception {
        mockMvc.perform(get("/create-account"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userEntity"))
                .andExpect(view().name("/create-account"));
    }

    @Test
    public void should_returnBindingErrors_whenPostCreatePartialAccount() throws Exception {
        UserEntity userEntityPartial = Faker.getFakeUserEntity();
        userEntityPartial.setFullName("");

        mockMvc.perform(post("/create-account")
                        .with(csrf())
                        .flashAttr("userEntity", userEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userEntity"))
                .andExpect(view().name("/create-account"));
    }

    @Test
    public void should_createUser_whenPostCreateNewAccount() throws Exception {
        when(userService.createUser(any())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(post("/create-account")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightCreatedUser", true))
                .andExpect(view().name("redirect:/user/home"));
    }

    @Test
    public void should_notCreateUser_whenPostCreateExistingAccount() throws Exception {
        when(userService.createUser(any(UserEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/create-account")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("wrongCreatedUser", true))
                .andExpect(view().name("/create-account"));
    }
}
