package com.poseidon.app.integrationtests;

import com.poseidon.app.dal.entity.BidListEntity;
import com.poseidon.app.dal.repository.BidListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BidListRepositoryIT {

    @Autowired
    private BidListRepository bidListRepository;

    @Test
    public void bidListTest() {
        BidListEntity bid = new BidListEntity("Account Test", "Type Test", 10d);

        // Save
        bid = bidListRepository.save(bid);
        assertNotNull(bid.getBidListId());
        assertEquals(bid.getBidQuantity(), 10d, 10d);

        // Update
        bid.setBidQuantity(20d);
        bid = bidListRepository.save(bid);
        assertEquals(bid.getBidQuantity(), 20d, 20d);

        // Find
        List<BidListEntity> listResult = bidListRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = bid.getBidListId();
        bidListRepository.delete(bid);
        Optional<BidListEntity> bidList = bidListRepository.findById(id);
        assertFalse(bidList.isPresent());
    }
}