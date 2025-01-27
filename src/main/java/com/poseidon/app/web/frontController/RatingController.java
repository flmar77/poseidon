package com.poseidon.app.web.frontController;

import com.poseidon.app.dal.entity.RatingEntity;
import com.poseidon.app.domain.helper.UserHelper;
import com.poseidon.app.domain.service.RatingService;
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
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/rating/list")
    public String getRatingList(Model model, Authentication authentication) {
        log.debug("get all ratings");
        model.addAttribute("ratingEntities", ratingService.getAllRatings());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/rating/list";
    }

    @GetMapping("/rating/add")
    public String getRatingAdd(Model model, Authentication authentication) {
        model.addAttribute("ratingEntity", new RatingEntity());
        model.addAttribute("username", UserHelper.getUserName(authentication));
        return "/rating/add";
    }

    @PostMapping("/rating/add")
    public String postRatingAdd(@Valid @ModelAttribute RatingEntity ratingEntity,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rating/add";
        }
        try {
            RatingEntity ratingEntitySaved = ratingService.createRating(ratingEntity);
            log.debug("rating created with id : " + ratingEntitySaved.getId());
            redirectAttributes.addFlashAttribute("rightCreatedRating", true);
            return "redirect:/rating/list";
        } catch (EntityExistsException e) {
            log.error("rating not created because account already exists : " + ratingEntity.getOrderNumber());
            model.addAttribute("wrongCreatedRating", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rating/add";
        }
    }

    @GetMapping("/rating/update/{id}")
    public String getRatingUpdate(@PathVariable("id") Integer id,
                                  Model model,
                                  RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            RatingEntity ratingEntity = ratingService.getRatingById(id);
            model.addAttribute("ratingEntity", ratingEntity);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rating/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing rating with id : " + id);
            redirectAttributes.addFlashAttribute("missingRatingId", true);
            return "redirect:/rating/list";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String postRatingUpdate(@PathVariable("id") Integer id,
                                   @Valid @ModelAttribute RatingEntity ratingEntity,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rating/update";
        }

        try {
            ratingEntity.setId(id);
            RatingEntity ratingEntitySaved = ratingService.updateRating(ratingEntity);
            log.debug("rating updated with id : " + ratingEntitySaved.getId());
            model.addAttribute("rightUpdatedRating", true);
            model.addAttribute("username", UserHelper.getUserName(authentication));
            return "/rating/update";
        } catch (NoSuchElementException e) {
            log.error("can't update missing rating with id : " + id);
            redirectAttributes.addFlashAttribute("missingRatingId", true);
            return "redirect:/rating/list";
        }
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        try {
            ratingService.deleteRatingById(id);
            log.debug("rating deleted with id : " + id);
            redirectAttributes.addFlashAttribute("rightDeletedRating", true);
        } catch (NoSuchElementException e) {
            log.error("can't delete missing rating with id : " + id);
            redirectAttributes.addFlashAttribute("missingRatingId", true);
        }
        return "redirect:/rating/list";
    }

}
