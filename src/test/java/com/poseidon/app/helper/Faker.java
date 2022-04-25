package com.poseidon.app.helper;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.dal.entity.UserEntity;

public abstract class Faker {

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
        bidEntity.setId(1);
        bidEntity.setAccount("accountTest");
        bidEntity.setType("typeTest");
        bidEntity.setBidQuantity(111.11D);
        bidEntity.setAskQuantity(null);
        bidEntity.setBid(null);
        bidEntity.setAsk(null);
        bidEntity.setBenchmark(null);
        bidEntity.setBidListDate(null);
        bidEntity.setCommentary(null);
        bidEntity.setSecurity(null);
        bidEntity.setStatus(null);
        bidEntity.setTrader(null);
        bidEntity.setBook(null);
        bidEntity.setCreationName(null);
        bidEntity.setCreationDate(null);
        bidEntity.setRevisionName(null);
        bidEntity.setRevisionDate(null);
        bidEntity.setDealName(null);
        bidEntity.setDealType(null);
        bidEntity.setSourceListId(null);
        bidEntity.setSide(null);
        return bidEntity;
    }

    public static CurveEntity getFakeCurveEntity() {
        CurveEntity curveEntity = new CurveEntity();
        curveEntity.setId(1);
        curveEntity.setCurveId(1);
        curveEntity.setTerm(1.11D);
        curveEntity.setValue(2.22D);
        curveEntity.setAsOfDate(null);
        curveEntity.setCreationDate(null);
        return curveEntity;
    }
}
