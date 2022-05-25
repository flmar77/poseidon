package com.poseidon.app.unittests.web.frontController;

import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.domain.service.CurveService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.frontController.CurveController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CurveController.class)
public class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurveService curveService;

    @SuppressWarnings("unused")
    @MockBean
    private UserService userService;
    @SuppressWarnings("unused")
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    public void should_returnRightCurveList_whenGetCurveList() throws Exception {
        when(curveService.getAllCurves()).thenReturn(Collections.singletonList(Faker.getFakeCurveEntity()));

        mockMvc.perform(get("/curve/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/curve/list"))
                .andExpect(model().attributeExists("curveEntities"))
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeCurveEntity().getCurveId()))));
    }

    @Test
    @WithMockUser
    public void should_returnRightCurveAdd_whenGetCurveAdd() throws Exception {
        mockMvc.perform(get("/curve/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curveEntity"))
                .andExpect(view().name("/curve/add"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialCurveAdd() throws Exception {
        CurveEntity curveEntityPartial = Faker.getFakeCurveEntity();
        curveEntityPartial.setCurveId(-1);

        mockMvc.perform(post("/curve/add")
                        .with(csrf())
                        .flashAttr("curveEntity", curveEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("curveEntity"))
                .andExpect(view().name("/curve/add"));
    }

    @Test
    @WithMockUser
    public void should_createCurve_whenPostNewCurveAdd() throws Exception {
        when(curveService.createCurve(any())).thenReturn(Faker.getFakeCurveEntity());

        mockMvc.perform(post("/curve/add")
                        .with(csrf())
                        .flashAttr("curveEntity", Faker.getFakeCurveEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightCreatedCurve", true))
                .andExpect(view().name("redirect:/curve/list"));
    }

    @Test
    @WithMockUser
    public void should_notCreateCurve_whenPostExistingCurveAdd() throws Exception {
        when(curveService.createCurve(any(CurveEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/curve/add")
                        .with(csrf())
                        .flashAttr("curveEntity", Faker.getFakeCurveEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("wrongCreatedCurve", true))
                .andExpect(view().name("/curve/add"));
    }

    @Test
    @WithMockUser
    public void should_returnCurveUpdate_whenGetExistingCurveUpdate() throws Exception {
        when(curveService.getCurveById(anyInt())).thenReturn(Faker.getFakeCurveEntity());

        mockMvc.perform(get("/curve/update/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/curve/update"));
    }

    @Test
    @WithMockUser
    public void should_notReturnCurveUpdate_whenGetMissingCurveUpdate() throws Exception {
        when(curveService.getCurveById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/curve/update/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingCurveId", true))
                .andExpect(view().name("redirect:/curve/list"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialCurveUpdate() throws Exception {
        CurveEntity curveEntityPartial = Faker.getFakeCurveEntity();
        curveEntityPartial.setCurveId(-1);

        mockMvc.perform(post("/curve/update/1")
                        .with(csrf())
                        .flashAttr("curveEntity", curveEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("curveEntity"))
                .andExpect(view().name("/curve/update"));
    }

    @Test
    @WithMockUser
    public void should_updateCurve_whenPostExistingCurveUpdate() throws Exception {
        when(curveService.updateCurve(any())).thenReturn(Faker.getFakeCurveEntity());

        mockMvc.perform(post("/curve/update/1")
                        .with(csrf())
                        .flashAttr("curveEntity", Faker.getFakeCurveEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("rightUpdatedCurve", true))
                .andExpect(view().name("/curve/update"));
    }

    @Test
    @WithMockUser
    public void should_notUpdateCurve_whenPostMissingCurveUpdate() throws Exception {
        when(curveService.updateCurve((any(CurveEntity.class)))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/curve/update/1")
                        .with(csrf())
                        .flashAttr("curveEntity", Faker.getFakeCurveEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingCurveId", true))
                .andExpect(view().name("redirect:/curve/list"));
    }

    @Test
    @WithMockUser
    public void should_deleteCurve_whenGetExistingCurveDelete() throws Exception {
        mockMvc.perform(get("/curve/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightDeletedCurve", true))
                .andExpect(view().name("redirect:/curve/list"));
    }

    @Test
    @WithMockUser
    public void should_notDeleteCurve_whenGetMissingCurveDelete() throws Exception {
        doThrow(NoSuchElementException.class).when(curveService).deleteCurveById(anyInt());

        mockMvc.perform(get("/curve/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingCurveId", true))
                .andExpect(view().name("redirect:/curve/list"));
    }

}
