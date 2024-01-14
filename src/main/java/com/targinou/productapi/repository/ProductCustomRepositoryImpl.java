package com.targinou.productapi.repository;

import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String INITIAL_QUERY = "SELECT p FROM Product p JOIN p.category c ";
    private static final String WHERE_CLAUSE = "WHERE p.active = true ";

    @Override
    public Page<Product> searchByFilter(ProductSearchFilterDTO searchDTO) {
        StringBuilder whereClause = new StringBuilder(WHERE_CLAUSE);

        if (searchDTO.getId() != null) {
            whereClause.append("AND p.id = :id ");
        }
        if (searchDTO.getName() != null && !searchDTO.getName().isEmpty()) {
            whereClause.append("AND LOWER(p.name) LIKE LOWER(:name) ");
        }
        if (searchDTO.getSku() != null && !searchDTO.getSku().isEmpty()) {
            whereClause.append("AND p.sku LIKE :sku ");
        }
        if (searchDTO.getCategoryId() != null) {
            whereClause.append("AND c.id = :categoryId ");
        }
        if (searchDTO.getUserId() != null) {
            whereClause.append("AND p.user.id = :userId ");
        }
        if (searchDTO.getCostPrice() != null) {
            whereClause.append("AND p.costPrice = :costPrice ");
        }
        if (searchDTO.getIcms() != null) {
            whereClause.append("AND p.icms = :icms ");
        }
        if (searchDTO.getSalePrice() != null) {
            whereClause.append("AND p.salePrice = :salePrice ");
        }
        if (searchDTO.getStockQuantity() != null) {
            whereClause.append("AND p.stockQuantity = :stockQuantity ");
        }

        String orderByField = searchDTO.getOrderBy();
        String orderDirection = searchDTO.getOrderDirection().toUpperCase();

        String orderBy = "ORDER BY p." + orderByField + " " + orderDirection;

        int pageNumber = searchDTO.getPageable().getPageNumber();
        int pageSize = searchDTO.getPageable().getPageSize();

        String finalQuery = INITIAL_QUERY + whereClause + orderBy;

        Query query = entityManager.createQuery(finalQuery, Product.class);
        setQueryParameters(query, searchDTO);

        query.setMaxResults(pageSize);
        query.setFirstResult(pageNumber * pageSize);

        var resultList = query.getResultList();
        long totalResults = countFilteredResults(whereClause.toString(), searchDTO);

        return new PageImpl<>(resultList, PageRequest.of(pageNumber, pageSize), totalResults);
    }

    private long countFilteredResults(String whereClause, ProductSearchFilterDTO searchDTO) {
        Query countQuery = entityManager.createQuery(
                "SELECT COUNT(p) FROM Product p JOIN p.category c " + whereClause);
        setQueryParameters(countQuery, searchDTO);
        return (long) countQuery.getSingleResult();
    }

    private void setQueryParameters(Query query, ProductSearchFilterDTO searchDTO) {
        if (searchDTO.getId() != null) {
            query.setParameter("id", searchDTO.getId());
        }
        if (searchDTO.getName() != null && !searchDTO.getName().isEmpty()) {
            query.setParameter("name", "%" + searchDTO.getName() + "%");
        }
        if (searchDTO.getSku() != null && !searchDTO.getSku().isEmpty()) {
            query.setParameter("sku", "%" + searchDTO.getSku() + "%");
        }
        if (searchDTO.getCategoryId() != null) {
            query.setParameter("categoryId", searchDTO.getCategoryId());
        }
        if (searchDTO.getUserId() != null) {
            query.setParameter("userId", searchDTO.getUserId());
        }
        if (searchDTO.getCostPrice() != null) {
            query.setParameter("costPrice", searchDTO.getCostPrice());
        }
        if (searchDTO.getIcms() != null) {
            query.setParameter("icms", searchDTO.getIcms());
        }
        if (searchDTO.getSalePrice() != null) {
            query.setParameter("salePrice", searchDTO.getSalePrice());
        }
        if (searchDTO.getStockQuantity() != null) {
            query.setParameter("stockQuantity", searchDTO.getStockQuantity());
        }
    }
}