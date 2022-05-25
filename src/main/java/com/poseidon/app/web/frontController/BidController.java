package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.domain.helper.UserHelper;
import com.poseidon.app.domain.service.BidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public String getBidList(Model model,
                             Authentication authentication) {
        log.debug("get all bids");
        model.addAttribute("bidEntities", bidService.getAllBids());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/bid/list";
    }

    @GetMapping("/bid/add")
    public String getBidAdd(Model model,
                            Authentication authentication) {
        model.addAttribute("bidEntity", new BidEntity());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/bid/add";
    }

    @PostMapping("/bid/add")
    public String postBidAdd(@Valid @ModelAttribute BidEntity bidEntity,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/bid/add";
        }
        try {
            BidEntity bidEntitySaved = bidService.createBid(bidEntity);
            log.debug("bid created with id : " + bidEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedBid", true);
            return "redirect:/bid/list";
        } catch (EntityExistsException e) {
            log.error("bid not created because account already exists : " + bidEntity.getAccount());
            model.addAttribute("wrongCreatedBid", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/bid/add";
        }
    }

    @GetMapping("/bid/update/{id}")
    public String getBidUpdate(@PathVariable("id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication) {
        try {
            BidEntity bidEntity = bidService.getBidById(id);
            model.addAttribute("bidEntity", bidEntity);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/bid/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing bid with id : " + id);
            redirectAttributes.addFlashAttribute("missingBidId", true);
            return "redirect:/bid/list";
        }
    }

    @PostMapping("/bid/update/{id}")
    public String postBidUpdate(@PathVariable("id") Integer id,
                                @Valid @ModelAttribute BidEntity bidEntity,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/bid/update";
        }

        try {
            bidEntity.setId(id);
            BidEntity bidEntitySaved = bidService.updateBid(bidEntity);
            log.debug("bid updated with id : " + bidEntitySaved.getId());
            model.addAttribute("rightUpdatedBid", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/bid/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing bid with id : " + id);
            redirectAttributes.addFlashAttribute("missingBidId", true);
            return "redirect:/bid/list";
        } catch (EntityExistsException e) {
            log.error("bid not updated because account already exists : " + bidEntity.getAccount());
            model.addAttribute("wrongUpdatedBid", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/bid/update";
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
            log.error("can't delete missing bid with id : " + id);
            redirectAttributes.addFlashAttribute("missingBidId", true);
        }
        return "redirect:/bid/list";
    }

}
