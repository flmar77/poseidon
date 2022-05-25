package com.poseidon.app.web.apiController;

import com.poseidon.app.dal.entity.RuleEntity;
import com.poseidon.app.domain.service.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/rule")
public class RuleRestController {

    @Autowired
    private RuleService ruleService;

    @GetMapping()
    public List<RuleEntity> getRules() {
        log.debug("get all rules");
        return ruleService.getAllRules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRule(@PathVariable Integer id) {
        try {
            RuleEntity ruleEntity = ruleService.getRuleById(id);
            log.debug("successfully get rule/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(ruleEntity);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while getting rule because of missing rule with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

    @PostMapping()
    public ResponseEntity<?> postRule(@Valid @RequestBody RuleEntity ruleEntity,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while posting rule because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            RuleEntity ruleEntitySaved = ruleService.createRule(ruleEntity);
            log.debug("successfully post rule");
            return ResponseEntity.status(HttpStatus.CREATED).body(ruleEntitySaved);
        } catch (EntityExistsException e) {
            String logAndBodyMessage = "error while posting rule because already existing rule with name=" + ruleEntity.getName();
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putRule(@PathVariable Integer id,
                                     @Valid @RequestBody RuleEntity ruleEntity,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while putting rule because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
        if (id.intValue() != ruleEntity.getId().intValue()) {
            String logAndBodyMessage = "error while putting rule because of inconsistent ids";
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            RuleEntity ruleEntitySaved = ruleService.updateRule(ruleEntity);
            log.debug("successfully put rule/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(ruleEntitySaved);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while putting rule because missing rule with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable Integer id) {
        try {
            ruleService.deleteRuleById(id);
            String logAndBodyMessage = "successfully delete rule/" + id;
            log.debug(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.OK).body(logAndBodyMessage);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while deleting rule because of missing rule with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

}
