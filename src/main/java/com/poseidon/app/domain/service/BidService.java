package com.poseidon.app.domain.service;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.dal.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    public List<BidEntity> getAllBids() {
        return bidRepository.findAll();
    }

    public BidEntity createBid(BidEntity bidEntity) throws EntityExistsException {
        checkUniqueness(bidEntity.getAccount());
        return saveBid(bidEntity);
    }

    public BidEntity getBidById(Integer id) throws NoSuchElementException {
        return bidRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public BidEntity updateBid(BidEntity bidEntity) throws NoSuchElementException, EntityExistsException {
        checkExistenceOfBidById(bidEntity.getId());
        checkUniqueness(bidEntity.getAccount());
        return saveBid(bidEntity);
    }

    public void deleteBidById(Integer id) throws NoSuchElementException {
        checkExistenceOfBidById(id);
        bidRepository.deleteById(id);
    }

    private void checkExistenceOfBidById(Integer id) {
        bidRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private void checkUniqueness(String fieldToCheck) {
        if (bidRepository.findByAccount(fieldToCheck).isPresent()) {
            throw new EntityExistsException();
        }
    }

    private BidEntity saveBid(BidEntity bidEntityToSave) {
        return bidRepository.save(bidEntityToSave);
    }
}
