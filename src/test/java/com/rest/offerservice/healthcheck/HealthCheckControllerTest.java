package com.rest.offerservice.healthcheck;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = HealthCheckController.class, secure = false)
public class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthCheckService healthCheckService;

    @Test
    public void testHealthOfApplication() throws Exception {
        Mockito.when(healthCheckService.checkHealth()).thenReturn("Application is OK!");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/health").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "Application is OK!";

        assertEquals(expected,result.getResponse().getContentAsString());
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());


    }
}
