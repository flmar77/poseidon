package com.poseidon.app.web.apiController;

import com.poseidon.app.dal.entity.TradeEntity;
import com.poseidon.app.domain.service.TradeService;
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
@RequestMapping("/api/trade")
public class TradeRestController {

    @Autowired
    private TradeService tradeService;

    @GetMapping()
    public List<TradeEntity> getTrades() {
        log.debug("get all trades");
        return tradeService.getAllTrades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrade(@PathVariable Integer id) {
        try {
            TradeEntity tradeEntity = tradeService.getTradeById(id);
            log.debug("successfully get trade/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(tradeEntity);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while getting trade because of missing trade with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

    @PostMapping()
    public ResponseEntity<?> postTrade(@Valid @RequestBody TradeEntity tradeEntity,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while posting trade because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            TradeEntity tradeEntitySaved = tradeService.createTrade(tradeEntity);
            log.debug("successfully post trade");
            return ResponseEntity.status(HttpStatus.CREATED).body(tradeEntitySaved);
        } catch (EntityExistsException e) {
            String logAndBodyMessage = "error while posting trade because already existing trade with account=" + tradeEntity.getAccount();
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putTrade(@PathVariable Integer id,
                                      @Valid @RequestBody TradeEntity tradeEntity,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while putting trade because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
        if (id.intValue() != tradeEntity.getId().intValue()) {
            String logAndBodyMessage = "error while putting trade because of inconsistent ids";
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            TradeEntity tradeEntitySaved = tradeService.updateTrade(tradeEntity);
            log.debug("successfully put trade/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(tradeEntitySaved);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while putting trade because missing trade with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrade(@PathVariable Integer id) {
        try {
            tradeService.deleteTradeById(id);
            String logAndBodyMessage = "successfully delete trade/" + id;
            log.debug(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.OK).body(logAndBodyMessage);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while deleting trade because of missing trade with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

}
