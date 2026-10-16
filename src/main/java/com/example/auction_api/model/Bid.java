package com.example.auction_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {
    private Long id;
    private Long lotId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime time;

    public Bid(Long id, Long lotId, Long userId, BigDecimal amount, LocalDateTime time) {
        this.id = id;
        this.lotId = lotId;
        this.userId = userId;
        this.amount = amount;
        this.time = time;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}