package com.example.demo_ass.service.impl;

import com.example.demo_ass.entity.OrderDetail;
import com.example.demo_ass.repository.OrderDetailRepository;
import com.example.demo_ass.service.OrderDetailService;
import com.example.demo_ass.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public OrderDetail findById(Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        // Tự động lấy giá sản phẩm nếu chưa có
        if (orderDetail.getUnitPrice() == null || orderDetail.getUnitPrice().compareTo(BigDecimal.ZERO) == 0) {
            if (orderDetail.getProduct() != null && orderDetail.getProduct().getId() != null) {
                var product = productService.findById(orderDetail.getProduct().getId());
                if (product != null && product.getPrice() != null) {
                    orderDetail.setUnitPrice(product.getPrice());
                }
            }
        }
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }
}