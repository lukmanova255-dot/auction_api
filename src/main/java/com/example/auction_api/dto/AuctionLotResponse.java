package com.example.auction_api.dto;

import com.example.auction_api.model.AuctionLot;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionLotResponse {
    private Long id;
    private Long itemId;
    private BigDecimal startPrice;
    private BigDecimal bidStep;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public AuctionLotResponse(Long id, Long itemId, BigDecimal startPrice, BigDecimal bidStep,
                              LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.id = id;
        this.itemId = itemId;
        this.startPrice = startPrice;
        this.bidStep = bidStep;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public static AuctionLotResponse from(AuctionLot lot) {
        return new AuctionLotResponse(
                lot.getId(), lot.getItemId(), lot.getStartPrice(), lot.getBidStep(),
                lot.getStartTime(), lot.getEndTime(), lot.getStatus()
        );
    }

    public Long getId() { return id; }
    public Long getItemId() { return itemId; }
    public BigDecimal getStartPrice() { return startPrice; }
    public BigDecimal getBidStep() { return bidStep; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
}