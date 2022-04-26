package com.poseidon.app.web.apiController;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.domain.service.BidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class BidRestController {

    @Autowired
    private BidService bidService;

    @GetMapping("/bid/list")
    public List<BidEntity> getBidList() {
        log.debug("get all bids");
        return bidService.getAllBids();
    }


}
