package com.homequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
class SellerControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private SellerDAO sellerDAO;

  @Nested
  @DisplayName("POST /api/sellers/register")
  class RegisterTests {

    @Test
    @DisplayName("201 when registration succeeds")
    void registerSucceeds() throws Exception {
      Seller input = new Seller();
      input.setFullName("Alice");
      input.setEmail("alice@example.com");
      input.setPassword("secret");

      Seller created = new Seller(123, "Alice", "alice@example.com", null, true, null, null, "Free");
      given(sellerDAO.createSeller(any(Seller.class))).willReturn(created);

      mvc.perform(post("/api/sellers/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(input)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.sellerID").value(123))
        .andExpect(jsonPath("$.fullName").value("Alice"))
        .andExpect(jsonPath("$.email").value("alice@example.com"))
        .andExpect(jsonPath("$.password").doesNotExist()); 
    }

    @Test
    @DisplayName("400 when registration fails (duplicate email)")
    void registerFails() throws Exception {
      given(sellerDAO.createSeller(any())).willReturn(null);

      Seller bad = new Seller();
      bad.setFullName("");
      bad.setEmail("alice@example.com");
      bad.setPassword("pw");

      mvc.perform(post("/api/sellers/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(bad)))
        .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("POST /api/sellers/login")
  class LoginTests {

    @Test
    @DisplayName("200 and body when credentials correct")
    void loginSucceeds() throws Exception {
      Seller loginReq = new Seller();
      loginReq.setEmail("bob@example.com");
      loginReq.setPassword("hunter2");

      Seller dbSeller = new Seller(456, "Bob", "bob@example.com", null, true, null, null, "Free");
      given(sellerDAO.getSellerByEmailAndPassword("bob@example.com", "hunter2"))
        .willReturn(dbSeller);

      mvc.perform(post("/api/sellers/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(loginReq)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sellerID").value(456))
        .andExpect(jsonPath("$.email").value("bob@example.com"))
        .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("401 when credentials invalid")
    void loginFails() throws Exception {
      Seller wrong = new Seller();
      wrong.setEmail("noone@nowhere");
      wrong.setPassword("badpw");

      given(sellerDAO.getSellerByEmailAndPassword(anyString(), anyString()))
        .willReturn(null);

      mvc.perform(post("/api/sellers/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(wrong)))
        .andExpect(status().isUnauthorized());
    }
  }
}
