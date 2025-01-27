package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.RuleEntity;
import com.poseidon.app.domain.helper.UserHelper;
import com.poseidon.app.domain.service.RuleService;
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
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @GetMapping("/rule/list")
    public String getRuleList(Model model, Authentication authentication) {
        log.debug("get all rules");
        model.addAttribute("ruleEntities", ruleService.getAllRules());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/rule/list";
    }

    @GetMapping("/rule/add")
    public String getRuleAdd(Model model, Authentication authentication) {
        model.addAttribute("ruleEntity", new RuleEntity());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/rule/add";
    }

    @PostMapping("/rule/add")
    public String postRuleAdd(@Valid @ModelAttribute RuleEntity ruleEntity,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rule/add";
        }
        try {
            RuleEntity ruleEntitySaved = ruleService.createRule(ruleEntity);
            log.debug("rule created with id : " + ruleEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedRule", true);
            return "redirect:/rule/list";
        } catch (EntityExistsException e) {
            log.error("rule not created because account already exists : " + ruleEntity.getName());
            model.addAttribute("wrongCreatedRule", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rule/add";
        }
    }

    @GetMapping("/rule/update/{id}")
    public String getRuleUpdate(@PathVariable("id") Integer id,
                                Model model,
                                RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            RuleEntity ruleEntity = ruleService.getRuleById(id);
            model.addAttribute("ruleEntity", ruleEntity);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rule/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing rule with id : " + id);
            redirectAttributes.addFlashAttribute("missingRuleId", true);
            return "redirect:/rule/list";
        }
    }

    @PostMapping("/rule/update/{id}")
    public String postRuleUpdate(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute RuleEntity ruleEntity,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rule/update";
        }

        try {
            ruleEntity.setId(id);
            RuleEntity ruleEntitySaved = ruleService.updateRule(ruleEntity);
            log.debug("rule updated with id : " + ruleEntitySaved.getId());
            model.addAttribute("rightUpdatedRule", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rule/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing rule with id : " + id);
            redirectAttributes.addFlashAttribute("missingRuleId", true);
            return "redirect:/rule/list";
        }
    }

    @GetMapping("/rule/delete/{id}")
    public String deleteRule(@PathVariable("id") Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            ruleService.deleteRuleById(id);
            log.debug("rule deleted with id : " + id);
            redirectAttributes.addFlashAttribute("rightDeletedRule", true);
        } catch (NoSuchElementException e) {
            log.error("can't delete missing rule with id : " + id);
            redirectAttributes.addFlashAttribute("missingRuleId", true);
        }
        return "redirect:/rule/list";
    }

}
