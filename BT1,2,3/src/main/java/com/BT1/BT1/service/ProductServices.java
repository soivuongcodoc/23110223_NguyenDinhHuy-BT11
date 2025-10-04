package com.BT1.BT1.service;

import com.BT1.BT1.entity.Product;

import java.util.List;

public interface ProductServices {
    void delete(Long id);
    Product get(Long id);
    Product save(Product product);
    List<Product> listAll();
}
