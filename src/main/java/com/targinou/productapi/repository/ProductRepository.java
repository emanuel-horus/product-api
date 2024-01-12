package com.targinou.productapi.repository;


import com.targinou.productapi.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends GenericRepository<Product>, ProductCustomRepository {

}
