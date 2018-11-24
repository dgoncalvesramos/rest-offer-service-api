package com.rest.offerservice.offer;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "OFFER")
public class OfferEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    @CreationTimestamp
    private Instant currentDate;
    private String description;
    @Column(nullable = false)
    private Instant expirationDate;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private OfferStatus status;

    public OfferEntity() { }

    public OfferEntity(Instant currentDate, String description, Instant expirationDate, BigDecimal price, OfferStatus status) {
        this.currentDate = currentDate;
        this.description = description;
        this.expirationDate = expirationDate;
        this.price = price;
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrentDate(Instant currentDate) {
        this.currentDate = currentDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Instant getCurrentDate() {
        return currentDate;
    }

    public String getDescription() {
        return description;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OfferStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferEntity)) return false;
        OfferEntity that = (OfferEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCurrentDate(), that.getCurrentDate()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getExpirationDate(), that.getExpirationDate()) &&
                Objects.equals(getPrice(), that.getPrice()) &&
                getStatus() == that.getStatus();
    }

    @Override
    public String toString() {
        return "OfferEntity{" +
                "id=" + id +
                ", currentDate=" + currentDate +
                ", description='" + description + '\'' +
                ", expirationDate=" + expirationDate +
                ", price=" + price +
                ", status=" + status +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCurrentDate(), getDescription(), getExpirationDate(), getPrice(), getStatus());
    }
}
