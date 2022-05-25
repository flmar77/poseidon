package com.poseidon.app.unittests.web.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.domain.service.CurveService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.apiController.CurveRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CurveRestController.class)
public class CurveRestControllerTest {

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
    public void should_returnRightCurveList_whenGetCurve() throws Exception {
        when(curveService.getAllCurves()).thenReturn(Collections.singletonList(Faker.getFakeCurveEntity()));

        mockMvc.perform(get("/api/curve"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeCurveEntity().getCurveId()))));
    }

    @Test
    public void should_returnRightCurve_whenGetCurveId() throws Exception {
        when(curveService.getCurveById(anyInt())).thenReturn(Faker.getFakeCurveEntity());

        mockMvc.perform(get("/api/curve/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeCurveEntity().getCurveId()))));
    }

    @Test
    public void should_returnNotFound_whenGetMissingCurveId() throws Exception {
        when(curveService.getCurveById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/curve/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("error")));
    }

    @Test
    public void should_returnBindingErrors_whenPostPartialCurve() throws Exception {
        CurveEntity curveEntityPartial = Faker.getFakeCurveEntity();
        curveEntityPartial.setCurveId(null);
        String inputJson = new ObjectMapper().writeValueAsString(curveEntityPartial);

        mockMvc.perform(post("/api/curve")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_createCurve_whenPostNewCurve() throws Exception {
        when(curveService.createCurve(any())).thenReturn(Faker.getFakeCurveEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeCurveEntity());

        mockMvc.perform(post("/api/curve")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_notCreateCurve_whenPostExistingCurve() throws Exception {
        when(curveService.createCurve(any(CurveEntity.class))).thenThrow(EntityExistsException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeCurveEntity());

        mockMvc.perform(post("/api/curve")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnBindingErrors_whenPutPartialCurve() throws Exception {
        CurveEntity curveEntityPartial = Faker.getFakeCurveEntity();
        curveEntityPartial.setCurveId(null);
        String inputJson = new ObjectMapper().writeValueAsString(curveEntityPartial);

        mockMvc.perform(put("/api/curve/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnUnprocessableEntity_whenPutInconsistentCurve() throws Exception {
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeCurveEntity());

        mockMvc.perform(put("/api/curve/2")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_updateCurve_whenPutExistingCurve() throws Exception {
        when(curveService.updateCurve(any())).thenReturn(Faker.getFakeCurveEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeCurveEntity());

        mockMvc.perform(put("/api/curve/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_notUpdateCurve_whenPutMissingCurve() throws Exception {
        when(curveService.updateCurve(any())).thenThrow(NoSuchElementException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeCurveEntity());

        mockMvc.perform(put("/api/curve/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_deleteCurve_whenDeleteExistingCurve() throws Exception {
        mockMvc.perform(delete("/api/curve/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_NotDeleteCurve_whenDeleteMissingCurve() throws Exception {
        doThrow(new NoSuchElementException()).when(curveService).deleteCurveById(anyInt());

        mockMvc.perform(delete("/api/curve/1"))
                .andExpect(status().isNotFound());
    }

}
