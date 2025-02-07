package kz.amihady.app.service;


import kz.amihady.app.customer.entity.Customer;
import kz.amihady.app.customer.repository.CustomerRepository;
import kz.amihady.app.customer.service.impl.CustomerServiceImpl;
import kz.amihady.app.customer.service.mapper.CustomerMapper;
import kz.amihady.app.customer.service.request.CustomerCreateRequest;
import kz.amihady.app.customer.service.request.CustomerUpdateRequest;
import kz.amihady.app.customer.service.response.CustomerResponse;
import kz.amihady.app.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;


    @Test
    void findAllCustomers_ShouldReturnCustomerResponses_WhenCustomersExist() {
        List<Customer> customers = List.of(new Customer(1L, "John", "Doe", "john@example.com", null));
        List<CustomerResponse> customerResponses = List.of(new CustomerResponse(1L, "John", "Doe", "john@example.com",null));

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.fromCustomer(any())).thenReturn(customerResponses.get(0));

        List<CustomerResponse> result = customerService.findAllCustomers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).firstName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void createCustomer_ShouldSaveCustomer_WhenValidRequest() {
        CustomerCreateRequest request = new CustomerCreateRequest("John", "Doe", "john@example.com", null);
        Customer customer = new Customer(1L, "John", "Doe", "john@example.com", null);
        CustomerResponse response = new CustomerResponse(1L, "John", "Doe", "john@example.com",null);

        when(customerMapper.toEntity(request)).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerMapper.fromCustomer(customer)).thenReturn(response);

        CustomerResponse result = customerService.createCustomer(request);

        assertNotNull(result);
        assertEquals("John", result.firstName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateCustomer_ShouldThrowException_WhenCustomerNotFound() {
        Long customerId = 1L;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Updated", "User", "updated@example.com",null);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(customerId, updateRequest));

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void findById_ShouldReturnCustomerResponse_WhenCustomerExists() {
        Long customerId = 1L;
        Customer customer = new Customer(customerId, "John", "Doe", "john@example.com", null);
        CustomerResponse response = new CustomerResponse(customerId, "John", "Doe", "john@example.com",null);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomer(customer)).thenReturn(response);

        CustomerResponse result = customerService.findById(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.id());
    }

    @Test
    void findById_ShouldThrowCustomerNotFoundException_WhenCustomerDoesNotExist() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(customerId));

        verify(customerMapper, never()).fromCustomer(any());
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer_WhenExists() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void deleteCustomer_ShouldNotDeleteCustomer_WhenNotExists() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, never()).deleteById(anyLong());
    }


    @Test
    void existsById_ShouldReturnTrue_WhenCustomerExists() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));

        boolean exists = customerService.existsById(customerId);

        assertTrue(exists);
        verify(customerRepository, times(1)).findById(customerId);
    }


}