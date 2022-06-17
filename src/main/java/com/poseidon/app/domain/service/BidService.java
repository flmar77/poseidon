package com.poseidon.app.domain.service;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.dal.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    public List<BidEntity> getAllBids() {
        return bidRepository.findAll();
    }

    public BidEntity createBid(BidEntity bidEntity) throws EntityExistsException {
        if (bidRepository.findByAccount(bidEntity.getAccount()).isPresent()) {
            throw new EntityExistsException();
        }
        return saveBid(bidEntity);
    }

    public BidEntity getBidById(Integer id) throws NoSuchElementException {
        return bidRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public BidEntity updateBid(BidEntity bidEntity) throws NoSuchElementException, EntityExistsException {
        checkExistenceOfBidById(bidEntity.getId());

        Optional<BidEntity> optionalBidEntity = bidRepository.findByAccount(bidEntity.getAccount());
        if (optionalBidEntity.isPresent()) {
            if (!optionalBidEntity.get().getId().equals(bidEntity.getId())) {
                throw new EntityExistsException();
            }
        }

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

    private BidEntity saveBid(BidEntity bidEntityToSave) {
        return bidRepository.save(bidEntityToSave);
    }
}
