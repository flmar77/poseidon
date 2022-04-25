package com.poseidon.app.helper;

import com.poseidon.app.dal.entity.*;

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
        BidEntity bidEntity = new BidEntity("accountTest", "typeTest", 111.11D);
        bidEntity.setId(1);
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
        CurveEntity curveEntity = new CurveEntity(1, 1.11D, 2.22D);
        curveEntity.setId(1);
        curveEntity.setAsOfDate(null);
        curveEntity.setCreationDate(null);
        return curveEntity;
    }

    public static RatingEntity getFakeRatingEntity() {
        RatingEntity ratingEntity = new RatingEntity(1, "moodysRatingTest", "sandPratingTest", "fitchRatingTest");
        ratingEntity.setId(1);
        return ratingEntity;
    }

    public static RuleEntity getFakeRuleEntity() {
        RuleEntity ruleEntity = new RuleEntity("nameTest", "descriptionTest", "jsonTest", "templateTest", "sqlStrTest", "sqlPartTest");
        ruleEntity.setId(1);
        return ruleEntity;
    }
}
