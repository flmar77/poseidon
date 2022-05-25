package com.poseidon.app.unittests.web.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.apiController.UserRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @SuppressWarnings("unused")
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_returnRightUserList_whenGetUser() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(Faker.getFakeUserEntity()));

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeUserEntity().getUserName()))));
    }

    @Test
    public void should_returnRightUser_whenGetUserId() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(Faker.getFakeUserEntity());

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeUserEntity().getUserName()))));
    }

    @Test
    public void should_returnNotFound_whenGetMissingUserId() throws Exception {
        when(userService.getUserById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("error")));
    }

    @Test
    public void should_returnBindingErrors_whenPostPartialUser() throws Exception {
        UserEntity userEntityPartial = Faker.getFakeUserEntity();
        userEntityPartial.setUserName("");
        String inputJson = new ObjectMapper().writeValueAsString(userEntityPartial);

        mockMvc.perform(post("/api/user")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_createUser_whenPostNewUser() throws Exception {
        when(userService.createUser(any())).thenReturn(Faker.getFakeUserEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeUserEntity());

        mockMvc.perform(post("/api/user")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_notCreateUser_whenPostExistingUser() throws Exception {
        when(userService.createUser(any(UserEntity.class))).thenThrow(EntityExistsException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeUserEntity());

        mockMvc.perform(post("/api/user")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnBindingErrors_whenPutPartialUser() throws Exception {
        UserEntity userEntityPartial = Faker.getFakeUserEntity();
        userEntityPartial.setUserName("");
        String inputJson = new ObjectMapper().writeValueAsString(userEntityPartial);

        mockMvc.perform(put("/api/user/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnUnprocessableEntity_whenPutInconsistentUser() throws Exception {
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeUserEntity());

        mockMvc.perform(put("/api/user/2")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_updateUser_whenPutExistingUser() throws Exception {
        when(userService.updateUser(any())).thenReturn(Faker.getFakeUserEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeUserEntity());

        mockMvc.perform(put("/api/user/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_notUpdateUser_whenPutMissingUser() throws Exception {
        when(userService.updateUser(any())).thenThrow(NoSuchElementException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeUserEntity());

        mockMvc.perform(put("/api/user/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_deleteUser_whenDeleteExistingUser() throws Exception {
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_NotDeleteUser_whenDeleteMissingUser() throws Exception {
        doThrow(new NoSuchElementException()).when(userService).deleteUserById(anyInt());

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

}
