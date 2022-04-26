package com.poseidon.app.integrationtests;

import com.poseidon.app.dal.entity.RuleEntity;
import com.poseidon.app.dal.repository.RuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RuleRepositoryIT {

    @Autowired
    private RuleRepository ruleRepository;

    @Test
    public void ruleTest() {
        RuleEntity rule = new RuleEntity("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

        // Save
        rule = ruleRepository.save(rule);
        assertNotNull(rule.getId());
        assertTrue(rule.getName().equals("Rule Name"));

        // Update
        rule.setName("Rule Name Update");
        rule = ruleRepository.save(rule);
        assertTrue(rule.getName().equals("Rule Name Update"));

        // Find
        List<RuleEntity> listResult = ruleRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = rule.getId();
        ruleRepository.delete(rule);
        Optional<RuleEntity> ruleList = ruleRepository.findById(id);
        assertFalse(ruleList.isPresent());
    }
}
