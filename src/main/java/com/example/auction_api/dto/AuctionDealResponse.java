package com.example.auction_api.dto;

import com.example.auction_api.model.AuctionDeal;

import java.math.BigDecimal;

public class AuctionDealResponse {
    private Long id;
    private Long lotId;
    private Long winningBidId;
    private BigDecimal amount;
    private String transferStatus;

    public AuctionDealResponse(Long id, Long lotId, Long winningBidId, BigDecimal amount, String transferStatus) {
        this.id = id;
        this.lotId = lotId;
        this.winningBidId = winningBidId;
        this.amount = amount;
        this.transferStatus = transferStatus;
    }

    public static AuctionDealResponse from(AuctionDeal deal) {
        return new AuctionDealResponse(
                deal.getId(),
                deal.getLot() != null ? deal.getLot().getId() : null,
                deal.getWinningBid() != null ? deal.getWinningBid().getId() : null,
                deal.getAmount(),
                deal.getTransferStatus()
        );
    }

    public Long getId() { return id; }
    public Long getLotId() { return lotId; }
    public Long getWinningBidId() { return winningBidId; }
    public BigDecimal getAmount() { return amount; }
    public String getTransferStatus() { return transferStatus; }
}