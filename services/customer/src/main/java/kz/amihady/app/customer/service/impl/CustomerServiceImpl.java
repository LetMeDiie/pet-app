package kz.amihady.app.customer.service.impl;

import kz.amihady.app.customer.repository.CustomerRepository;
import kz.amihady.app.customer.service.CustomerService;
import kz.amihady.app.customer.service.mapper.CustomerMapper;
import kz.amihady.app.customer.service.request.CustomerCreateRequest;
import kz.amihady.app.customer.service.request.CustomerUpdateRequest;
import kz.amihady.app.customer.service.response.CustomerResponse;
import kz.amihady.app.exception.CustomerNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    @Override
    public List<CustomerResponse> findAllCustomers() {
        log.info("Получение всех клиентов из репозитория");
        List<CustomerResponse> customers = customerRepository.findAll()
                .stream()
                .map(this.customerMapper::fromCustomer)
                .collect(Collectors.toList());
        log.info("Найдено {} клиентов", customers.size());
        return customers;
    }

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest createRequest) {
        log.info("Создание нового клиента");
        var customer = customerMapper.toEntity(createRequest);
        customerRepository.save(customer);
        log.info("Клиент с id: {} успешно создан", customer.getId());
        return customerMapper.fromCustomer(customer);
    }

    @Override
    public void updateCustomer(Long id,CustomerUpdateRequest request) {
        log.info("Начинаем обновление клиента с id: {}", id);

        var customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Клиент с указанным идентификатором {} не найден для обновления", id);
                    return new CustomerNotFoundException(
                            String.format("Не удалось обновить клиента. Клиент с указанным идентификатором %s не найден:", id)
                    );
                });

        log.info("Клиент с id: {} найден, начинаем обновление", id);
        log.debug("Данные клиента до обновления: {}", customer);

        customerMapper.updateCustomerFromRequest(customer, request);

        log.debug("Данные клиента после обновления: {}", customer);

        customerRepository.save(customer);

        log.info("Клиент с id: {} успешно обновлен", id);

    }

    @Override
    public CustomerResponse findById(Long id) {
        log.info("Получение клиента с id: {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> {
                    log.error("Клиент с id: {} не найден", id);
                    return new CustomerNotFoundException(String.format("Клиент с указанным идентификатором %s не найден", id));
                });
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Удаление клиента с id: {}", id);
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            log.info("Клиент с id: {} успешно удален", id);
        } else {
            log.warn("Клиент с id: {} не существует, операция удаления пропущена", id);
        }
    }

    @Override
    public Boolean existsById(Long id) {
        log.info("Проверка существования клиента с id: {}", id);
        Boolean exists = customerRepository.findById(id).isPresent();
        log.info("Клиент с id: {} существует: {}", id, exists);
        return exists;
    }
}
