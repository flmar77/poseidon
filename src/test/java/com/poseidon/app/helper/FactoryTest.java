package com.poseidon.app.helper;

import com.poseidon.app.dal.entity.UserEntity;

public abstract class FactoryTest {

    public static UserEntity getFakeUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUserName("userNameTest");
        userEntity.setPassword("passwordTest");
        userEntity.setFullName("fullNameTest");
        userEntity.setRole("USER");
        return userEntity;
    }

}
