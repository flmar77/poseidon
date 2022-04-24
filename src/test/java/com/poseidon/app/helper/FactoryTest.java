package com.poseidon.app.helper;

import com.poseidon.app.dal.entity.BidEntity;
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

    public static BidEntity getFakeBidEntity() {
        BidEntity bidEntity = new BidEntity();
        bidEntity.setAccount("accountTest");
        bidEntity.setType("typeTest");
        bidEntity.setBidQuantity(111.11D);
        return bidEntity;
    }
}
