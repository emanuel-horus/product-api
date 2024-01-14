package com.targinou.productapi.model;

import com.targinou.productapi.model.enums.ActionType;
import jakarta.persistence.*;

@Entity
@Table(name = "audit_log")
public class AuditLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description", length = 2000)
    private String description;

    public AuditLog() {
    }

    public AuditLog(Product product, ActionType actionType, User user, String description) {
        this.product = product;
        this.actionType = actionType;
        this.user = user;
        this.description = description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
