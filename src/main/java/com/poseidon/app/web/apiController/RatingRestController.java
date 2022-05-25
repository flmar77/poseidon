package com.poseidon.app.web.apiController;

import com.poseidon.app.dal.entity.RatingEntity;
import com.poseidon.app.domain.service.RatingService;
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
@RequestMapping("/api/rating")
public class RatingRestController {

    @Autowired
    private RatingService ratingService;

    @GetMapping()
    public List<RatingEntity> getRatings() {
        log.debug("get all ratings");
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRating(@PathVariable Integer id) {
        try {
            RatingEntity ratingEntity = ratingService.getRatingById(id);
            log.debug("successfully get rating/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(ratingEntity);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while getting rating because of missing rating with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

    @PostMapping()
    public ResponseEntity<?> postRating(@Valid @RequestBody RatingEntity ratingEntity,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while posting rating because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            RatingEntity ratingEntitySaved = ratingService.createRating(ratingEntity);
            log.debug("successfully post rating");
            return ResponseEntity.status(HttpStatus.CREATED).body(ratingEntitySaved);
        } catch (EntityExistsException e) {
            String logAndBodyMessage = "error while posting rating because already existing rating with orderNumber=" + ratingEntity.getOrderNumber();
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putRating(@PathVariable Integer id,
                                       @Valid @RequestBody RatingEntity ratingEntity,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while putting rating because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
        if (id.intValue() != ratingEntity.getId().intValue()) {
            String logAndBodyMessage = "error while putting rating because of inconsistent ids";
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            RatingEntity ratingEntitySaved = ratingService.updateRating(ratingEntity);
            log.debug("successfully put rating/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(ratingEntitySaved);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while putting rating because missing rating with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Integer id) {
        try {
            ratingService.deleteRatingById(id);
            String logAndBodyMessage = "successfully delete rating/" + id;
            log.debug(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.OK).body(logAndBodyMessage);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while deleting rating because of missing rating with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

}
