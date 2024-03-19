package com.fiap.techchallenge.order.application.useCases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fiap.techchallenge.order.application.exceptions.GetProductException;
import com.fiap.techchallenge.order.application.exceptions.MakeOrderException;
import com.fiap.techchallenge.order.application.services.OrderService;
import com.fiap.techchallenge.order.application.services.ProductService;
import com.fiap.techchallenge.order.domain.entity.Order;
import com.fiap.techchallenge.order.domain.entity.Product;
import com.fiap.techchallenge.order.gateway.exception.GeneratePaymentQRCodeException;
import com.fiap.techchallenge.order.gateway.port.GeneratePaymentQRCodePort;
import com.fiap.techchallenge.order.gateway.port.SendRequestedPaymentPort;
import com.fiap.techchallenge.order.util.ConstantsUtil;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MakeOrderUseCases {

    private final OrderService orderService;
    private final ProductService productService;
    private final GeneratePaymentQRCodePort generatePaymentQRCodePort;
    private final SendRequestedPaymentPort sendRequestedPaymentPort;

    public Result makeOrder(Command cmd) throws MakeOrderException {
        try {
            Order order = createNewOrder(cmd);

            orderService.saveOrder(order);

            String qrCode = generatePaymentQRCode(order);

            sendRequestedPaymentPort.send(order.getId());

            return Result.builder()
                    .orderId(order.getId())
                    .qrCode(qrCode)
                    .build();
        } catch (Exception e) {
            log.error("MakeOrderUseCases: Failed to make order: {}", e.getMessage());

            throw new MakeOrderException(e.getMessage());
        }
    }

    private Order createNewOrder(Command cmd) throws GetProductException {
        Order order = Order.builder()
                .clientName(cmd.clientName())
                .customerId(cmd.customerId())
                .paymentMethod(cmd.paymentMethod())
                .build();

        for (Command.OrderItem orderItem : cmd.orderItemList) {
            Product product = productService.findProduct(orderItem.productId());

            Order.Item item = Order.Item.builder()
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .additionalInfo(orderItem.additionalInfo())
                    .productId(product.getId())
                    .quantity(orderItem.quantity())
                    .build();

            order.addItem(item);
        }

        return order;
    }

    private String generatePaymentQRCode(Order order) throws GeneratePaymentQRCodeException {
        GeneratePaymentQRCodePort.Request request = GeneratePaymentQRCodePort.Request.builder()
                .orderId(order.getId())
                .quantity(Long.valueOf(order.getTotalItems()))
                .price(order.getTotalPrice())
                .title(ConstantsUtil.PAYMENT_TITLE_PREFIX + order.getClientName())
                .build();

        return generatePaymentQRCodePort.generate(request);
    }

    @Builder
    public record Command(
            String clientName,
            String customerId,
            String paymentMethod,
            List<OrderItem> orderItemList) {

        @Builder
        public record OrderItem(
                long productId,
                long quantity,
                String additionalInfo) {
        }
    }

    @Builder
    public record Result(
            Long orderId,
            String qrCode) {
    }

}
