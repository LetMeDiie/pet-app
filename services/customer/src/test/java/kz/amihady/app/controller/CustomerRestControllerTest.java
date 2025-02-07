package kz.amihady.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import kz.amihady.app.customer.controller.CustomerRestController;
import kz.amihady.app.customer.service.CustomerService;
import kz.amihady.app.customer.service.request.AddressUpdateRequest;
import kz.amihady.app.customer.service.request.CustomerUpdateRequest;
import kz.amihady.app.customer.service.response.CustomerResponse;
import kz.amihady.app.exception.CustomerNotFoundException;
import kz.amihady.app.handler.CustomerExceptionHandler;
import kz.amihady.app.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerRestController.class)
@ContextConfiguration(classes = CustomerRestController.class)
@Import({CustomerExceptionHandler.class,GlobalExceptionHandler.class})
class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private static final Long CUSTOMER_ID = 123L;

    @Test
    @DisplayName("Должен вернуть CustomerResponse по ID")
    void findById_ReturnsCustomerResponse() throws Exception {
        CustomerResponse response = new CustomerResponse( CUSTOMER_ID,"John", "Doe", "john.doe@example.com", null);
        when(customerService.findById(CUSTOMER_ID)).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/{customer-id}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(customerService, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    @DisplayName("Должен вернуть 404 и JSON с ошибкой в ErrorResponse, если клиента нет")
    void findById_ReturnsNotFound_WhenCustomerNotExists() throws Exception {
        when(customerService.findById(CUSTOMER_ID))
                .thenThrow(new CustomerNotFoundException("Клиент не найден"));

        mockMvc.perform(get("/api/v1/customers/{customer-id}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors['Ошибка']").value("Клиент не найден"));

        verify(customerService, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    @DisplayName("Должен вернуть true, если клиент существует")
    void existsById_ReturnsTrue() throws Exception {
        when(customerService.existsById(CUSTOMER_ID)).thenReturn(true);

        mockMvc.perform(get("/api/v1/customers/{customer-id}/exists", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(customerService, times(1)).existsById(CUSTOMER_ID);
    }

    @Test
    @DisplayName("Должен удалить клиента и вернуть 204 No Content")
    void delete_ReturnsNoContent() throws Exception {
        doNothing().when(customerService).deleteCustomer(CUSTOMER_ID);

        mockMvc.perform(delete("/api/v1/customers/{customer-id}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(CUSTOMER_ID);
    }

    @Test
    public void testUpdateCustomerSuccess() throws Exception {
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "John", "Doe", "john.doe@example.com",
                new AddressUpdateRequest("Main St", "123", "12345"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/customers/{customer-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isAccepted());

        verify(customerService, times(1)).updateCustomer(eq(1L), eq(request));
    }

    @Test
    public void testUpdateCustomerValidationErrorFirstNameTooShort() throws Exception {
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "J", "Doe", "john.doe@example.com",
                new AddressUpdateRequest("Main St", "123", "12345"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/customers/{customer-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").value("Имя должно содержать от 2 до 10 символов."));
    }

    @Test
    public void testUpdateCustomerInvalidEmail() throws Exception {
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "John", "Doe", "invalid-email",
                new AddressUpdateRequest("Main St", "123", "12345"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/customers/{customer-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Некорректный формат email."));
    }

    @Test
    public void updateCustomer_ShouldReturnBadRequest_WhenRequestBodyIsMissing() throws Exception {
        mockMvc.perform(put("/api/v1/customers/{customer-id}", CUSTOMER_ID )
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errors['Ошибка']").value("Тело запроса отсутствует или некорректно"));
    }
}
