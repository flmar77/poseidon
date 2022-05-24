package com.poseidon.app.web.apiController;

import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.domain.service.CurveService;
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
@RequestMapping("/api/curve")
public class CurveRestController {

    @Autowired
    private CurveService curveService;

    @GetMapping()
    public List<CurveEntity> getCurves() {
        log.debug("get all curves");
        return curveService.getAllCurves();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCurve(@PathVariable Integer id) {
        try {
            CurveEntity curveEntity = curveService.getCurveById(id);
            log.debug("successfully get curve/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(curveEntity);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while getting curve because of missing curve with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

    @PostMapping()
    public ResponseEntity<?> postCurve(@Valid @RequestBody CurveEntity curveEntity,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while posting curve because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            CurveEntity curveEntitySaved = curveService.createCurve(curveEntity);
            log.debug("successfully post curve");
            return ResponseEntity.status(HttpStatus.CREATED).body(curveEntitySaved);
        } catch (EntityExistsException e) {
            String logAndBodyMessage = "error while posting curve because already existing curve with curveId=" + curveEntity.getCurveId();
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putCurve(@PathVariable Integer id,
                                      @Valid @RequestBody CurveEntity curveEntity,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String logAndBodyMessage = "error while putting curve because of wrong input data : "
                    + bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).
                    collect(Collectors.joining(", "));
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
        if (id.intValue() != curveEntity.getId().intValue()) {
            String logAndBodyMessage = "error while putting curve because of inconsistent ids";
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }

        try {
            CurveEntity curveEntitySaved = curveService.updateCurve(curveEntity);
            log.debug("successfully put curve/" + id);
            return ResponseEntity.status(HttpStatus.OK).body(curveEntitySaved);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while putting curve because missing curve with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(logAndBodyMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurve(@PathVariable Integer id) {
        try {
            curveService.deleteCurveById(id);
            String logAndBodyMessage = "successfully delete curve/" + id;
            log.debug(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.OK).body(logAndBodyMessage);
        } catch (NoSuchElementException e) {
            String logAndBodyMessage = "error while deleting curve because of missing curve with id=" + id;
            log.error(logAndBodyMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logAndBodyMessage);
        }
    }

}
