package com.poseidon.app.web.apiController;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.domain.service.BidService;
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
@RequestMapping("/api/bid")
public class BidRestController {

    @Autowired
    private BidService bidService;

    @GetMapping()
    public List<BidEntity> getBids() {
        log.debug("get all bids");
        return bidService.getAllBids();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBid(@PathVariable Integer id) {
        try {
            BidEntity bidEntity = bidService.getBidById(id);
            log.debug("successfully get bid/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(bidEntity);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while getting bid because of missing bid with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

    @PostMapping()
    public ResponseEntity<?> postBid(@Valid @RequestBody BidEntity bidEntity,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while posting bid because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            BidEntity bidEntitySaved = bidService.createBid(bidEntity);
            log.debug("successfully post bid");
            return ResponseEntity.status(HttpStatus.CREATED).body(bidEntitySaved);
        } catch (EntityExistsException e) {
            String logAndBodyMessage = "error while posting bid because already existing bid with account=" + bidEntity.getAccount();
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putBid(@PathVariable Integer id,
                                    @Valid @RequestBody BidEntity bidEntity,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while putting bid because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
        if (id.intValue() != bidEntity.getId().intValue()) {
            String logAndBodyMessage = "error while putting bid because of inconsistent ids";
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            BidEntity bidEntitySaved = bidService.updateBid(bidEntity);
            log.debug("successfully put bid/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(bidEntitySaved);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while putting bid because missing bid with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        } catch (EntityExistsException e) {
            String logAndBodyMessage = "error while putting bid because already existing bid with account=" + bidEntity.getAccount();
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBid(@PathVariable Integer id) {
        try {
            bidService.deleteBidById(id);
            String logAndBodyMessage = "successfully delete bid/" + id;
            log.debug(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.OK).body(logAndBodyMessage);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while deleting bid because of missing bid with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

}
