package com.poseidon.app.integrationtests;

import com.poseidon.app.dal.entity.TradeEntity;
import com.poseidon.app.dal.repository.TradeRepository;
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
        TradeEntity tradeEntity = new TradeEntity("Trade Account", "Type", 0D);

        // Save
        tradeEntity = tradeRepository.save(tradeEntity);
        assertNotNull(tradeEntity.getId());
        assertEquals("Trade Account", tradeEntity.getAccount());

        // Update
        tradeEntity.setAccount("Trade Account Update");
        tradeEntity = tradeRepository.save(tradeEntity);
        assertEquals("Trade Account Update", tradeEntity.getAccount());

        // Find
        List<TradeEntity> listResult = tradeRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = tradeEntity.getId();
        tradeRepository.delete(tradeEntity);
        Optional<TradeEntity> tradeList = tradeRepository.findById(id);
        assertFalse(tradeList.isPresent());
    }
}
