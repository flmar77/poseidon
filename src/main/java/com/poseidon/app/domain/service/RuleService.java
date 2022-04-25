package com.poseidon.app.domain.service;

import com.poseidon.app.dal.entity.RuleEntity;
import com.poseidon.app.dal.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    public List<RuleEntity> getAllRules() {
        return ruleRepository.findAll();
    }

    public RuleEntity createRule(RuleEntity ruleEntity) throws EntityExistsException {
        if (ruleRepository.findByName(ruleEntity.getName()).isPresent()) {
            throw new EntityExistsException();
        }
        return saveRule(ruleEntity);
    }

    public RuleEntity getRuleById(Integer id) throws NoSuchElementException {
        return ruleRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public RuleEntity updateRule(RuleEntity ruleEntity) throws NoSuchElementException {
        checkExistenceOfRuleById(ruleEntity.getId());
        return saveRule(ruleEntity);
    }

    public void deleteRuleById(Integer id) throws NoSuchElementException {
        checkExistenceOfRuleById(id);
        ruleRepository.deleteById(id);
    }

    private void checkExistenceOfRuleById(Integer id) {
        ruleRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private RuleEntity saveRule(RuleEntity ruleEntityToSave) {
        return ruleRepository.save(ruleEntityToSave);
    }
}
