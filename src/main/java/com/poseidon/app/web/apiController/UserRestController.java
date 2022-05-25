package com.poseidon.app.web.apiController;

import com.poseidon.app.dal.entity.UserEntity;
import com.poseidon.app.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<UserEntity> getUsers() {
        log.debug("get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        try {
            UserEntity userEntity = userService.getUserById(id);
            log.debug("successfully get user/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(userEntity);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while getting user because of missing user with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

    @PostMapping()
    public ResponseEntity<?> postUser(@Valid @RequestBody UserEntity userEntity,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while posting user because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            UserEntity userEntitySaved = userService.createUser(userEntity);
            log.debug("successfully post user");
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntitySaved);
        } catch (EntityExistsException e) {
            String logAndBodyMessage = "error while posting user because already existing user with account=" + userEntity.getUserName();
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable Integer id,
                                     @Valid @RequestBody UserEntity userEntity,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while putting user because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
        if (id.intValue() != userEntity.getId().intValue()) {
            String logAndBodyMessage = "error while putting user because of inconsistent ids";
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            UserEntity userEntitySaved = userService.updateUser(userEntity);
            log.debug("successfully put user/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(userEntitySaved);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while putting user because missing user with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUserById(id);
            String logAndBodyMessage = "successfully delete user/" + id;
            log.debug(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.OK).body(logAndBodyMessage);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while deleting user because of missing user with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

}
