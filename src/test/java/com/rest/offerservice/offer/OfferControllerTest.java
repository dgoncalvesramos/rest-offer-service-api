package com.rest.offerservice.offer;

import com.rest.offerservice.exceptions.OfferBadFormatException;
import com.rest.offerservice.exceptions.OfferNotFoundException;
import com.rest.offerservice.exceptions.OfferPatchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OfferController.class, secure = false)
public class OfferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferServiceImpl offerService;

    @Test
    public void testRetrieveAExistingOffer() throws Exception {
        Instant now = Instant.now();
        OfferEntity offerEntityReturned = new OfferEntity(now, "First offer", now.plusSeconds(86400),
                new BigDecimal(10.5), OfferStatus.OPEN);
        ;
        offerEntityReturned.setId(1L);

        Mockito.when(offerService.retrieveOffer(1L)).thenReturn(Optional.of(offerEntityReturned));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/offers/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{id:1,description:\"First offer\",price:10.5,status:OPEN}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Mockito.reset(offerService);
    }

    @Test
    public void testRetrieveAllExistingOffer() throws Exception {
        Instant now = Instant.now();
        OfferEntity offerEntityReturnedA = new OfferEntity(now, "First offer", now.plusSeconds(86400),
                new BigDecimal(10.5), OfferStatus.OPEN);
        ;
        offerEntityReturnedA.setId(1L);
        OfferEntity offerEntityReturnedB = new OfferEntity(now, "Second offer", now.minusSeconds(86400),
                new BigDecimal(10.5), OfferStatus.OPEN);
        ;
        offerEntityReturnedB.setId(2L);

        List<OfferEntity> offerEntities = new ArrayList<>();
        offerEntities.add(offerEntityReturnedA);
        offerEntities.add(offerEntityReturnedB);

        Mockito.when(offerService.retrieveAllOffers()).thenReturn(offerEntities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/offers").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected =
                "[{id:1,description:\"First offer\",price:10.5,status:OPEN}," +
                        "{id:2,description:\"Second offer\",price:10.5,status:OPEN}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Mockito.reset(offerService);
    }

    @Test
    public void testRetrieveANonExistingOffe() throws Exception {
        Mockito.when(offerService.retrieveOffer(1L)).thenThrow(new OfferNotFoundException(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/offers/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"message\":\"OfferNotFoundException{offerId=1}\",\"details\":\"uri=/offers/1\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Mockito.reset(offerService);
    }

    @Test
    public void testCreateOfferWithExpirationDateNotProvided() throws Exception {

        Instant now = Instant.now();
        Instant expirationDate = now.plusSeconds(86400);

        OfferEntity offerEntityToCreate = new OfferEntity(now, "First offer", expirationDate,
                new BigDecimal(10.5), OfferStatus.OPEN);
        offerEntityToCreate.setId(1L);
        Mockito.when(offerService.createOffer(offerEntityToCreate)).thenThrow(new OfferBadFormatException(1L, "Expiration date must be provided for offer"));

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/offers")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("{\"id\":\"1\",\"currentDate\":\"" + now + "\",\"description\":\"First offer\",\"expirationDate\":\"" + expirationDate.toString() + "\",\"price\":\"10.5\",\"status\":\"OPEN\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"message\":\"OfferBadFormatException{offerId=1} - Expiration date must be provided for offer\",\"details\":\"uri=/offers\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        Mockito.reset(offerService);

    }

    @Test
    public void testCreateOfferWithPriceNotProvided() throws Exception {

        Instant now = Instant.now();
        Instant expirationDate = now.plusSeconds(86400);

        OfferEntity offerEntityToCreate = new OfferEntity(now, "First offer", expirationDate,
                new BigDecimal(0), OfferStatus.OPEN);
        offerEntityToCreate.setId(1L);
        Mockito.when(offerService.createOffer(offerEntityToCreate)).thenThrow(new OfferBadFormatException(1L, "Price must be provided for the offer"));

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/offers")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("{\"id\":\"1\",\"currentDate\":\"" + now + "\",\"description\":\"First offer\",\"expirationDate\":\"" + expirationDate.toString() + "\",\"price\":\"0\",\"status\":\"OPEN\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"message\":\"OfferBadFormatException{offerId=1} - Price must be provided for the offer\",\"details\":\"uri=/offers\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        Mockito.reset(offerService);

    }

    @Test
    public void testCreateOfferAlreadyExpired() throws Exception {

        Instant now = Instant.now();
        Instant expirationDate = now.minusSeconds(86400);

        OfferEntity offerEntityToCreate = new OfferEntity(now, "First offer", expirationDate,
                new BigDecimal(10.5), OfferStatus.OPEN);
        offerEntityToCreate.setId(1L);
        Mockito.when(offerService.createOffer(offerEntityToCreate)).thenThrow(new OfferBadFormatException(1L, "Expiration date must be after current date"));

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/offers")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("{\"id\":\"1\",\"currentDate\":\"" + now + "\",\"description\":\"First offer\",\"expirationDate\":\"" + expirationDate.toString() + "\",\"price\":\"10.5\",\"status\":\"OPEN\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"message\":\"OfferBadFormatException{offerId=1} - Expiration date must be after current date\",\"details\":\"uri=/offers\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        Mockito.reset(offerService);

    }

    @Test
    public void testCreateOffer() throws Exception {

        Instant now = Instant.now();
        Instant expirationDate = now.plusSeconds(86400);

        OfferEntity offerEntityToCreate = new OfferEntity(now, "First offer", expirationDate,
                new BigDecimal(10.5), OfferStatus.OPEN);
        offerEntityToCreate.setId(1L);

        URI location = new URI("/offers/1");
        Mockito.when(offerService.createOffer(offerEntityToCreate)).thenReturn(ResponseEntity.created(location).build());

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/offers")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("{\"id\":\"1\",\"currentDate\":\"" + now + "\",\"description\":\"First offer\",\"expirationDate\":\"" + expirationDate.toString() + "\",\"price\":\"10.5\",\"status\":\"OPEN\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        // Assert header location
        String expectedLocation = "/offers/1";
        assertEquals(expectedLocation,response.getHeader("Location"));
        Mockito.reset(offerService);
    }

    @Test
    public void testCancelAnOffer() throws Exception {
             URI location = new URI("/offers/1");
        Mockito.when(offerService.cancelOffer(OfferStatus.CLOSE,1L)).thenReturn(ResponseEntity.noContent().location(location).build());

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/offers/1")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("\"CLOSE\"")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        // Assert header location
        String expectedLocation = "/offers/1";
        assertEquals(expectedLocation,response.getHeader("Location"));
        Mockito.reset(offerService);
    }

    @Test
    public void testOpenAClosedOffer() throws Exception {
        URI location = new URI("/offers/1");
        Mockito.when(offerService.cancelOffer(OfferStatus.CLOSE,1L)).thenThrow(new OfferPatchException(1L,"You can only cancel an existing offer"));

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/offers/1")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("\"CLOSE\"")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"message\":\"OfferPatchException{offerId=1} - You can only cancel an existing offer\",\"details\":\"uri=/offers/1\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        Mockito.reset(offerService);
    }

    @Test
    public void testCancelAnUnexistingOffer() throws Exception {
        URI location = new URI("/offers/1");
        Mockito.when(offerService.cancelOffer(OfferStatus.CLOSE,1L)).thenThrow(new OfferPatchException(1L,"You can't cancel a non existing offer"));

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/offers/1")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("\"CLOSE\"")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"message\":\"OfferPatchException{offerId=1} - You can't cancel a non existing offer\",\"details\":\"uri=/offers/1\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        Mockito.reset(offerService);
    }

    @Test
    public void testCancelAClosedOffer() throws Exception {
        URI location = new URI("/offers/1");
        Mockito.when(offerService.cancelOffer(OfferStatus.CLOSE,1L)).thenThrow(new OfferPatchException(1L,"You can't cancel an offer already closed"));

        // Send offer entity as body to /offers
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/offers/1")
                .accept(MediaType.APPLICATION_JSON_UTF8).content("\"CLOSE\"")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"message\":\"OfferPatchException{offerId=1} - You can't cancel an offer already closed\",\"details\":\"uri=/offers/1\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        Mockito.reset(offerService);
    }

}
