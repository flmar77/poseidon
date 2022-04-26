package com.poseidon.app.domain.service;

import com.poseidon.app.dal.entity.TradeEntity;
import com.poseidon.app.dal.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public List<TradeEntity> getAllTrades() {
        return tradeRepository.findAll();
    }

    public TradeEntity createTrade(TradeEntity tradeEntity) throws EntityExistsException {
        if (tradeRepository.findByAccount(tradeEntity.getAccount()).isPresent()) {
            throw new EntityExistsException();
        }
        return saveTrade(tradeEntity);
    }

    public TradeEntity getTradeById(Integer id) throws NoSuchElementException {
        return tradeRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public TradeEntity updateTrade(TradeEntity tradeEntity) throws NoSuchElementException {
        checkExistenceOfTradeById(tradeEntity.getId());
        return saveTrade(tradeEntity);
    }

    public void deleteTradeById(Integer id) throws NoSuchElementException {
        checkExistenceOfTradeById(id);
        tradeRepository.deleteById(id);
    }

    private void checkExistenceOfTradeById(Integer id) {
        tradeRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private TradeEntity saveTrade(TradeEntity tradeEntityToSave) {
        return tradeRepository.save(tradeEntityToSave);
    }
}
