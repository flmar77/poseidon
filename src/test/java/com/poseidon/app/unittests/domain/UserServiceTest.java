package com.poseidon.app.unittests.domain;


import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.dal.repository.UserRepository;
import com.poseidon.app.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_returnSomething_whenLoadExistingUserByUsername() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThat(userService.loadUserByUsername(anyString())).isNotNull();
    }

    @Test
    public void should_throwUsernameNotFoundException_whenLoadMissingUserByUsername() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userService.loadUserByUsername(anyString()));
    }

    @Test
    public void should_saveUser_whenCreateNewUser() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("xxx");
        when(userRepository.save(any())).thenReturn(getFakeUserEntity());

        UserEntity userEntity = userService.createUser(getFakeUserEntity());

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getPassword()).isEqualTo("");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void should_throwEntityExistsException_whenCreateExistingUser() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> userService.createUser(getFakeUserEntity()));
    }

    @Test
    public void should_returnSomething_whenGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(getFakeUserEntity()));

        assertThat(userService.getAllUsers()).isNotNull();
    }

    @Test
    public void should_findUser_whenGetExistingUserById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(getFakeUserEntity()));

        UserEntity userEntity = userService.getUserById(anyInt());

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getPassword()).isEqualTo("");
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenGetExistingUserById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.getUserById(anyInt()));
    }

    @Test
    public void should_saveUser_whenUpdateExistingUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(getFakeUserEntity()));
        when(passwordEncoder.encode(anyString())).thenReturn("xxx");
        when(userRepository.save(any())).thenReturn(getFakeUserEntity());

        UserEntity userEntity = userService.updateUser(getFakeUserEntity());

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getPassword()).isEqualTo("");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void should_throwNoSuchElementException_whenUpdateMissingUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.updateUser(getFakeUserEntity()));
    }

    @Test
    public void should_deleteUser_whenDeleteExistingUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(getFakeUserEntity()));

        userService.deleteUserById(anyInt());

        verify(userRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenDeleteExistingUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.deleteUserById(anyInt()));
    }

    private UserEntity getFakeUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUserName("userNameTest");
        userEntity.setPassword("passwordTest");
        userEntity.setFullName("fullNameTest");
        userEntity.setRole("USER");
        return userEntity;
    }
}