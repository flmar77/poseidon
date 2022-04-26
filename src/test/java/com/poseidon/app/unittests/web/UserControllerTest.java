package com.poseidon.app.unittests.web;

import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.frontController.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @SuppressWarnings("unused")
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    public void should_returnRightUserHome_whenGetUserHome() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/home"))
                .andExpect(model().attributeExists("username"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    @WithMockUser
    public void should_returnUserUpdate_whenGetUserHeadAsUser() throws Exception {
        when(userService.getUserByUserName(anyString())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(get("/user/head"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/user-update/1"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_returnUserAdminList_whenGetUserHeadAsAdmin() throws Exception {
        UserEntity userEntity = Faker.getFakeUserEntity();
        userEntity.setUserName("admin");
        userEntity.setRole("ADMIN");
        when(userService.getUserByUserName(anyString())).thenReturn(userEntity);

        mockMvc.perform(get("/user/head"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/admin/list"));
    }

    @Test
    @WithMockUser
    public void should_returnUserUserUpdate_whenGetExistingUserUserUpdate() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(get("/user/user-update/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/user-update"));
    }

    @Test
    @WithMockUser
    public void should_redirectUserHome_whenGetMissingUserUserUpdate() throws Exception {
        when(userService.getUserById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/user/user-update/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/home"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialUserUserUpdate() throws Exception {
        UserEntity userEntityPartial = Faker.getFakeUserEntity();
        userEntityPartial.setFullName("");

        mockMvc.perform(post("/user/user-update/1")
                        .with(csrf())
                        .flashAttr("userEntity", userEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userEntity"))
                .andExpect(view().name("/user/user-update"));
    }

    @Test
    @WithMockUser
    public void should_updateUser_whenPostExistingUserUserUpdate() throws Exception {
        when(userService.updateUser(any())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(post("/user/user-update/1")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("rightUpdatedUser", true))
                .andExpect(view().name("/user/user-update"));
    }

    @Test
    @WithMockUser
    public void should_notUpdateUser_whenPostMissingUserUserUpdate() throws Exception {
        when(userService.updateUser((any(UserEntity.class)))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/user/user-update/1")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingUserId", true))
                .andExpect(view().name("redirect:/user/home"));
    }

    // ADMIN PART
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_returnRightUserAdminList_whenGetUserList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(Faker.getFakeUserEntity()));

        mockMvc.perform(get("/user/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/admin/list"))
                .andExpect(model().attributeExists("userEntities"))
                .andExpect(content().string(containsString("userNameTest")));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_returnRightUserAdminAdd_whenGetUserAdd() throws Exception {
        mockMvc.perform(get("/user/admin/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userEntity"))
                .andExpect(view().name("/user/admin/add"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_returnBindingErrors_whenPostPartialUserAdd() throws Exception {
        UserEntity userEntityPartial = Faker.getFakeUserEntity();
        userEntityPartial.setUserName("");

        mockMvc.perform(post("/user/admin/add")
                        .with(csrf())
                        .flashAttr("userEntity", userEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userEntity"))
                .andExpect(view().name("/user/admin/add"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_createUser_whenPostNewUserAdd() throws Exception {
        when(userService.createUser(any())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(post("/user/admin/add")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightCreatedUser", true))
                .andExpect(view().name("redirect:/user/admin/list"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_notCreateUser_whenPostExistingUserAdd() throws Exception {
        when(userService.createUser(any(UserEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/user/admin/add")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("wrongCreatedUser", true))
                .andExpect(view().name("/user/admin/add"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_returnUserUpdate_whenGetExistingUserUpdate() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(get("/user/admin/update/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/admin/update"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_notReturnUserUpdate_whenGetMissingUserUpdate() throws Exception {
        when(userService.getUserById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/user/admin/update/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingUserId", true))
                .andExpect(view().name("redirect:/user/admin/list"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_returnBindingErrors_whenPostPartialUserUpdate() throws Exception {
        UserEntity userEntityPartial = Faker.getFakeUserEntity();
        userEntityPartial.setUserName("");

        mockMvc.perform(post("/user/admin/update/1")
                        .with(csrf())
                        .flashAttr("userEntity", userEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userEntity"))
                .andExpect(view().name("/user/admin/update"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_updateUser_whenPostExistingUserUpdate() throws Exception {
        when(userService.updateUser(any())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(post("/user/admin/update/1")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("rightUpdatedUser", true))
                .andExpect(view().name("/user/admin/update"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_notUpdateUser_whenPostMissingUserUpdate() throws Exception {
        when(userService.updateUser((any(UserEntity.class)))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/user/admin/update/1")
                        .with(csrf())
                        .flashAttr("userEntity", Faker.getFakeUserEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingUserId", true))
                .andExpect(view().name("redirect:/user/admin/list"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_deleteUser_whenGetExistingUserDelete() throws Exception {
        mockMvc.perform(get("/user/admin/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightDeletedUser", true))
                .andExpect(view().name("redirect:/user/admin/list"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void should_notDeleteUser_whenGetMissingUserDelete() throws Exception {
        doThrow(NoSuchElementException.class).when(userService).deleteUserById(anyInt());

        mockMvc.perform(get("/user/admin/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingUserId", true))
                .andExpect(view().name("redirect:/user/admin/list"));
    }

}
