package com.poseidon.app.domain.service;

import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.dal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found : " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole()));

        return new User(
                userEntity.getUserName(),
                userEntity.getPassword(),
                authorities);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll().stream()
                .peek(userEntity -> userEntity.setPassword(""))
                .collect(Collectors.toList());
    }

    public UserEntity createUser(UserEntity userEntity) throws EntityExistsException {
        if (userRepository.findByUserName(userEntity.getUserName()).isPresent()) {
            throw new EntityExistsException();
        }
        return saveUserWithHashPassword(userEntity);
    }

    public UserEntity getUserById(Integer id) throws NoSuchElementException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        userEntity.setPassword("");
        return userEntity;
    }

    public UserEntity updateUser(UserEntity userEntity) throws NoSuchElementException {
        checkExistenceOfUserById(userEntity.getId());
        return saveUserWithHashPassword(userEntity);
    }

    public void deleteUserById(Integer id) throws NoSuchElementException {
        checkExistenceOfUserById(id);
        userRepository.deleteById(id);
    }

    public UserEntity getUserByUserName(String name) throws NoSuchElementException {
        return userRepository.findByUserName(name)
                .orElseThrow(NoSuchElementException::new);
    }

    private void checkExistenceOfUserById(Integer id) {
        userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private UserEntity saveUserWithHashPassword(UserEntity userEntityToSave) {
        userEntityToSave.setPassword(passwordEncoder.encode(userEntityToSave.getPassword()));
        UserEntity userEntitySaved = userRepository.save(userEntityToSave);
        userEntitySaved.setPassword("");
        return userEntitySaved;
    }
}
