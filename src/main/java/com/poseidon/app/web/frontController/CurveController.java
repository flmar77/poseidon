package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.domain.helper.UserHelper;
import com.poseidon.app.domain.service.CurveService;
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
public class CurveController {

    @Autowired
    private CurveService curveService;

    @GetMapping("/curve/list")
    public String getCurveList(Model model, Authentication authentication) {
        log.debug("get all curves");
        model.addAttribute("curveEntities", curveService.getAllCurves());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/curve/list";
    }

    @GetMapping("/curve/add")
    public String getCurveAdd(Model model, Authentication authentication) {
        model.addAttribute("curveEntity", new CurveEntity());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/curve/add";
    }

    @PostMapping("/curve/add")
    public String postCurveAdd(@Valid @ModelAttribute CurveEntity curveEntity,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/curve/add";
        }
        try {
            CurveEntity curveEntitySaved = curveService.createCurve(curveEntity);
            log.debug("curve created with id : " + curveEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedCurve", true);
            return "redirect:/curve/list";
        } catch (EntityExistsException e) {
            log.error("curve not created because account already exists : " + curveEntity.getCurveId());
            model.addAttribute("wrongCreatedCurve", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/curve/add";
        }
    }

    @GetMapping("/curve/update/{id}")
    public String getCurveUpdate(@PathVariable("id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            CurveEntity curveEntity = curveService.getCurveById(id);
            model.addAttribute("curveEntity", curveEntity);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/curve/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing curve with id : " + id);
            redirectAttributes.addFlashAttribute("missingCurveId", true);
            return "redirect:/curve/list";
        }
    }

    @PostMapping("/curve/update/{id}")
    public String postCurveUpdate(@PathVariable("id") Integer id,
                                  @Valid @ModelAttribute CurveEntity curveEntity,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/curve/update";
        }

        try {
            curveEntity.setId(id);
            CurveEntity curveEntitySaved = curveService.updateCurve(curveEntity);
            log.debug("curve updated with id : " + curveEntitySaved.getId());
            model.addAttribute("rightUpdatedCurve", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/curve/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing curve with id : " + id);
            redirectAttributes.addFlashAttribute("missingCurveId", true);
            return "redirect:/curve/list";
        }
    }

    @GetMapping("/curve/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes) {
        try {
            curveService.deleteCurveById(id);
            log.debug("curve deleted with id : " + id);
            redirectAttributes.addFlashAttribute("rightDeletedCurve", true);
        } catch (NoSuchElementException e) {
            log.error("can't delete missing curve with id : " + id);
            redirectAttributes.addFlashAttribute("missingCurveId", true);
        }
        return "redirect:/curve/list";
    }

}
