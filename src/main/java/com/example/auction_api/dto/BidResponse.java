package com.example.auction_api.dto;

import com.example.auction_api.model.Bid;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidResponse {
    private Long id;
    private Long lotId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime time;

    public BidResponse(Long id, Long lotId, Long userId, BigDecimal amount, LocalDateTime time) {
        this.id = id;
        this.lotId = lotId;
        this.userId = userId;
        this.amount = amount;
        this.time = time;
    }

    public static BidResponse from(Bid bid) {
        return new BidResponse(
                bid.getId(),
                bid.getLot() != null ? bid.getLot().getId() : null,
                bid.getUser() != null ? bid.getUser().getId() : null,
                bid.getAmount(),
                bid.getTime()
        );
    }

    public Long getId() { return id; }
    public Long getLotId() { return lotId; }
    public Long getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTime() { return time; }
}