package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.domain.service.BidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@Slf4j
@Controller
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping("/bid/list")
    public String getBidList(Model model) {
        log.debug("get all bids");
        model.addAttribute("bidEntities", bidService.getAllBids());
        return "/bid/list";
    }

    @GetMapping("/bid/add")
    public String getBidAdd(Model model) {
        model.addAttribute("bidEntity", new BidEntity());
        return "/bid/add";
    }

    @PostMapping("/bid/add")
    public String postBidAdd(@Valid @ModelAttribute BidEntity bidEntity,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            return "/bid/add";
        }
        try {
            BidEntity bidEntitySaved = bidService.createBid(bidEntity);
            log.debug("bid created with id : " + bidEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedBid", true);
            return "redirect:/bid/list";
        } catch (EntityExistsException e) {
            log.debug("bid not created because account already exists : " + bidEntity.getAccount());
            model.addAttribute("wrongCreatedBid", true);
            return "/bid/add";
        }
    }

    @GetMapping("/bid/update/{id}")
    public String getBidUpdate(@PathVariable("id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            BidEntity bidEntity = bidService.getBidById(id);
            model.addAttribute("bidEntity", bidEntity);
            return "/bid/update";
        } catch (NoSuchElementException e) {
            log.debug("can't update missing bid with id : " + id);
            redirectAttributes.addFlashAttribute("missingBidId", true);
            return "redirect:/bid/list";
        }
    }

    @PostMapping("/bid/update/{id}")
    public String postBidUpdate(@PathVariable("id") Integer id,
                                @Valid @ModelAttribute BidEntity bidEntity,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/bid/update";
        }

        try {
            bidEntity.setId(id);
            BidEntity bidEntitySaved = bidService.updateBid(bidEntity);
            log.debug("bid updated with id : " + bidEntitySaved.getId());
            model.addAttribute("rightUpdatedBid", true);
            return "/bid/update";
        } catch (NoSuchElementException e) {
            log.debug("can't update missing bid with id : " + id);
            redirectAttributes.addFlashAttribute("missingBidId", true);
            return "redirect:/bid/list";
        }
    }

    @GetMapping("/bid/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id,
                            RedirectAttributes redirectAttributes) {
        try {
            bidService.deleteBidById(id);
            log.debug("bid deleted with id : " + id);
            redirectAttributes.addFlashAttribute("rightDeletedBid", true);
        } catch (NoSuchElementException e) {
            log.debug("can't delete missing bid with id : " + id);
            redirectAttributes.addFlashAttribute("missingBidId", true);
        }
        return "redirect:/bid/list";
    }

}
