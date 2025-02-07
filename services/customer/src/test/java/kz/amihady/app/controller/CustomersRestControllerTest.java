package kz.amihady.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.amihady.app.customer.controller.CustomersRestController;
import kz.amihady.app.customer.service.CustomerService;
import kz.amihady.app.customer.service.request.AddressCreateRequest;
import kz.amihady.app.customer.service.request.CustomerCreateRequest;
import kz.amihady.app.customer.service.response.AddressResponse;
import kz.amihady.app.customer.service.response.CustomerResponse;
import kz.amihady.app.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomersRestController.class)
@ContextConfiguration(classes = CustomersRestController.class)
@Import(GlobalExceptionHandler.class)
class CustomersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private static final Long CUSTOMER_ID = 123L;

    @Test
    public void createCustomer_ShouldReturnCreated_WhenRequestIsValid() throws Exception {
        CustomerCreateRequest request = new CustomerCreateRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                new AddressCreateRequest("Street", "12", "12345")
        );

        AddressResponse addressResponse = new AddressResponse("Street", "12", "12345");
        CustomerResponse mockResponse = new CustomerResponse(1L, "John", "Doe", "john.doe@example.com", addressResponse);
        when(customerService.createCustomer(request)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/customers/1"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(customerService, times(1)).createCustomer(request);
    }

    @Test
    public void createCustomer_ShouldReturnBadRequest_WhenFirstNameIsTooShort() throws Exception {
        CustomerCreateRequest request = new CustomerCreateRequest(
                "J", // слишком короткое имя
                "Doe",
                "john.doe@example.com",
                new AddressCreateRequest("Street", "12", "12345")
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").value("Имя должно содержать от 2 до 10 символов."));
    }

    @Test
    public void createCustomer_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
        CustomerCreateRequest request = new CustomerCreateRequest(
                "John",
                "Doe",
                "invalid-email", // некорректный email
                new AddressCreateRequest("Street", "12", "12345")
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Некорректный формат email."));
    }

    @Test
    public void createCustomer_ShouldReturnBadRequest_WhenAddressIsNull() throws Exception {
        CustomerCreateRequest request = new CustomerCreateRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                null // отсутствует адрес
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.address").value("Адрес не может отсутствовать."));
    }

    @Test
    public void createCustomer_ShouldReturnBadRequest_WhenRequestIsNull() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['Ошибка']").value("Тело запроса отсутствует или некорректно"));
    }

    @Test
    public void findAll_ShouldReturnCustomers_WhenCustomersExist() throws Exception {
        List<CustomerResponse> customers = List.of(
                new CustomerResponse(1L, "John", "Doe", "john.doe@example.com", new AddressResponse("Street", "12", "12345")),
                new CustomerResponse(2L, "Jane", "Smith", "jane.smith@example.com", new AddressResponse("Avenue", "34", "67890"))
        );

        when(customerService.findAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(customerService, times(1)).findAllCustomers();
    }

    @Test
    public void findAll_ShouldReturnEmptyList_WhenNoCustomersExist() throws Exception {
        when(customerService.findAllCustomers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(customerService, times(1)).findAllCustomers();
    }

}