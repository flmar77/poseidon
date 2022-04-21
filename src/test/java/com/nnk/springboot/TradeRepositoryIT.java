package com.nnk.springboot;

import com.nnk.springboot.dal.entity.TradeEntity;
import com.nnk.springboot.dal.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TradeRepositoryIT {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void tradeTest() {
        TradeEntity tradeEntity = new TradeEntity("Trade Account", "Type");

        // Save
        tradeEntity = tradeRepository.save(tradeEntity);
        assertNotNull(tradeEntity.getTradeId());
        assertTrue(tradeEntity.getAccount().equals("Trade Account"));

        // Update
        tradeEntity.setAccount("Trade Account Update");
        tradeEntity = tradeRepository.save(tradeEntity);
        assertTrue(tradeEntity.getAccount().equals("Trade Account Update"));

        // Find
        List<TradeEntity> listResult = tradeRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = tradeEntity.getTradeId();
        tradeRepository.delete(tradeEntity);
        Optional<TradeEntity> tradeList = tradeRepository.findById(id);
        assertFalse(tradeList.isPresent());
    }
}
