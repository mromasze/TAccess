package pl.mromasze.taccess.bot.services.payment;

import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import pl.mromasze.taccess.bot.dto.PaymentResponse;
import pl.mromasze.taccess.bot.entity.Order;
import pl.mromasze.taccess.bot.enums.PaymentStatus;
import pl.mromasze.taccess.bot.events.OrderCompletedEvent;
import pl.mromasze.taccess.bot.repository.OrderRepository;

import java.util.List;

@Service
public class PaymentService {

    private final List<PaymentProcessor> processors;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(List<PaymentProcessor> processors, OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.processors = processors;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<PaymentProcessor> getEnabledProcessors() {
        return processors.stream()
                .filter(PaymentProcessor::isEnabled)
                .toList();
    }

    public PaymentResponse initiatePayment(Long orderId, String methodName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Prevent double payment
        if (order.getStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Order already paid.");
        }

        PaymentProcessor processor = processors.stream()
                .filter(p -> p.getMethodName().equalsIgnoreCase(methodName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Payment method not supported: " + methodName));

        PaymentResponse paymentInfo = processor.createPayment(order);
        
        order.setPaymentMethod(methodName.toUpperCase());
        // order.setTransactionId(...) // This would be set if the processor returned an ID object, for simplicity we return string
        orderRepository.save(order);

        return paymentInfo;
    }
    
    @org.springframework.transaction.annotation.Transactional
    public void completePayment(Long orderId, String transactionId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() == PaymentStatus.COMPLETED) {
            return; // Already processed
        }

        order.setStatus(PaymentStatus.COMPLETED);
        order.setTransactionId(transactionId);
        orderRepository.save(order);
        
        // Trigger product delivery logic via event
        eventPublisher.publishEvent(new OrderCompletedEvent(this, order));
    }
}