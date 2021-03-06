package dev.mouhieddine.springmvcrestexample.controllers.v1;

import dev.mouhieddine.springmvcrestexample.api.v1.model.CustomerDTO;
import dev.mouhieddine.springmvcrestexample.exceptions.ResourceNotFoundException;
import dev.mouhieddine.springmvcrestexample.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static dev.mouhieddine.springmvcrestexample.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Mouhieddine.dev
 * @since : 1/16/2021, Saturday
 **/
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
  public static final String FIRSTNAME = "Joe";
  public static final String LASTNAME = "Newman";
  @Mock
  CustomerService customerService;
  @InjectMocks
  CustomerController controller;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new RestResponseEntityExceptionHandler())
            .build();
  }

  @Test
  void getAllCustomers() throws Exception {
    // given
    CustomerDTO joe = new CustomerDTO();
    joe.setFirstname(FIRSTNAME);
    joe.setLastname("Newman");

    CustomerDTO faiz = new CustomerDTO();
    faiz.setFirstname("Faiz");
    faiz.setFirstname("Wasim");

    when(customerService.getAllCustomers()).thenReturn(Arrays.asList(joe, faiz));

    // then
    mockMvc.perform(get(CustomerController.BASE_URL)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customers", hasSize(2)));

  }

  @Test
  void getCustomerById() throws Exception {
    // given
    CustomerDTO joe = new CustomerDTO();
    joe.setFirstname(FIRSTNAME);
    joe.setLastname("Newman");
    joe.setCustomerUrl(CustomerController.BASE_URL + "/1");

    when(customerService.getCustomerById(anyLong())).thenReturn(joe);

    // then
    mockMvc.perform(get(CustomerController.BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname", equalTo(FIRSTNAME)));

  }

  @Test
  void createNewCustomer() throws Exception {

    //given
    CustomerDTO joe = new CustomerDTO();
    joe.setFirstname(FIRSTNAME);
    joe.setLastname("Newman");

    CustomerDTO returnJoe = new CustomerDTO();
    returnJoe.setFirstname(joe.getFirstname());
    returnJoe.setLastname(joe.getLastname());
    returnJoe.setCustomerUrl(CustomerController.BASE_URL + "/1");

    when(customerService.createNewCustomer(joe)).thenReturn(returnJoe);

    //when/then
    mockMvc.perform(post(CustomerController.BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(joe)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstname", equalTo(FIRSTNAME)))
            .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));
  }

  @Test
  void updateCustomerByDTO() throws Exception {
    //given
    CustomerDTO joe = new CustomerDTO();
    joe.setFirstname(FIRSTNAME);
    joe.setLastname(LASTNAME);

    CustomerDTO returnJoe = new CustomerDTO();
    returnJoe.setFirstname(joe.getFirstname());
    returnJoe.setLastname(joe.getLastname());
    returnJoe.setCustomerUrl(CustomerController.BASE_URL + "/1");

    when(customerService.saveCustomerByDTO(anyLong(), any(CustomerDTO.class))).thenReturn(returnJoe);

    //when/then
    mockMvc.perform(put(CustomerController.BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(joe)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname", equalTo(FIRSTNAME)))
            .andExpect(jsonPath("$.lastname", equalTo(LASTNAME)))
            .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));

  }

  @Test
  void patchCustomer() throws Exception {
    //given
    CustomerDTO joe = new CustomerDTO();
    joe.setFirstname(FIRSTNAME);

    CustomerDTO patchedJoe = new CustomerDTO();
    patchedJoe.setFirstname(joe.getFirstname());
    patchedJoe.setLastname(LASTNAME);
    patchedJoe.setCustomerUrl(CustomerController.BASE_URL + "/1");

    when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class)))
            .thenReturn(patchedJoe);

    //when/then
    mockMvc.perform(patch(CustomerController.BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(joe)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstname", equalTo(FIRSTNAME)))
            .andExpect(jsonPath("$.lastname", equalTo(LASTNAME)))
            .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));

  }

  @Test
  void deleteCustomer() throws Exception {
    mockMvc.perform(delete(CustomerController.BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    verify(customerService, times(1)).deleteCustomerById(anyLong());
  }

  @Test
  public void getCustomerByIdNotFound() throws Exception {

    when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

    mockMvc.perform(get(CustomerController.BASE_URL + "/10")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
  }
}