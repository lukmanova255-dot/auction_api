package com.example.auction_api.dto;

import java.math.BigDecimal;

public class BidRequest {
    private Long lotId;
    private Long userId;
    private BigDecimal amount;

    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}