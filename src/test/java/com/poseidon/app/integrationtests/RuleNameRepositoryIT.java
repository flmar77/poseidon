package com.poseidon.app.integrationtests;

import com.poseidon.app.dal.entity.RuleNameEntity;
import com.poseidon.app.dal.repository.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RuleNameRepositoryIT {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    public void ruleTest() {
        RuleNameEntity rule = new RuleNameEntity("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

        // Save
        rule = ruleNameRepository.save(rule);
        assertNotNull(rule.getId());
        assertTrue(rule.getName().equals("Rule Name"));

        // Update
        rule.setName("Rule Name Update");
        rule = ruleNameRepository.save(rule);
        assertTrue(rule.getName().equals("Rule Name Update"));

        // Find
        List<RuleNameEntity> listResult = ruleNameRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = rule.getId();
        ruleNameRepository.delete(rule);
        Optional<RuleNameEntity> ruleList = ruleNameRepository.findById(id);
        assertFalse(ruleList.isPresent());
    }
}