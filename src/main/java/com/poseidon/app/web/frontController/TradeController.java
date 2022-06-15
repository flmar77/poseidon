package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.TradeEntity;
import com.poseidon.app.domain.helper.UserHelper;
import com.poseidon.app.domain.service.TradeService;
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
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping("/trade/list")
    public String getTradeList(Model model, Authentication authentication) {
        log.debug("get all trades");
        model.addAttribute("tradeEntities", tradeService.getAllTrades());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/trade/list";
    }

    @GetMapping("/trade/add")
    public String getTradeAdd(Model model, Authentication authentication) {
        model.addAttribute("tradeEntity", new TradeEntity());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/trade/add";
    }

    @PostMapping("/trade/add")
    public String postTradeAdd(@Valid @ModelAttribute TradeEntity tradeEntity,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/trade/add";
        }
        try {
            TradeEntity tradeEntitySaved = tradeService.createTrade(tradeEntity);
            log.debug("trade created with id : " + tradeEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedTrade", true);
            return "redirect:/trade/list";
        } catch (EntityExistsException e) {
            log.error("trade not created because account already exists : " + tradeEntity.getAccount());
            model.addAttribute("wrongCreatedTrade", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/trade/add";
        }
    }

    @GetMapping("/trade/update/{id}")
    public String getTradeUpdate(@PathVariable("id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            TradeEntity tradeEntity = tradeService.getTradeById(id);
            model.addAttribute("tradeEntity", tradeEntity);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/trade/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing trade with id : " + id);
            redirectAttributes.addFlashAttribute("missingTradeId", true);
            return "redirect:/trade/list";
        }
    }

    @PostMapping("/trade/update/{id}")
    public String postTradeUpdate(@PathVariable("id") Integer id,
                                  @Valid @ModelAttribute TradeEntity tradeEntity,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/trade/update";
        }

        try {
            tradeEntity.setId(id);
            TradeEntity tradeEntitySaved = tradeService.updateTrade(tradeEntity);
            log.debug("trade updated with id : " + tradeEntitySaved.getId());
            model.addAttribute("rightUpdatedTrade", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/trade/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing trade with id : " + id);
            redirectAttributes.addFlashAttribute("missingTradeId", true);
            return "redirect:/trade/list";
        }
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes) {
        try {
            tradeService.deleteTradeById(id);
            log.debug("trade deleted with id : " + id);
            redirectAttributes.addFlashAttribute("rightDeletedTrade", true);
        } catch (NoSuchElementException e) {
            log.error("can't delete missing trade with id : " + id);
            redirectAttributes.addFlashAttribute("missingTradeId", true);
        }
        return "redirect:/trade/list";
    }

}
