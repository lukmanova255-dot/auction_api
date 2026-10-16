package com.example.auction_api.model;

import java.math.BigDecimal;

public class AuctionDeal {
    private Long id;
    private Long lotId;
    private Long winningBidId;
    private BigDecimal amount;
    private String transferStatus;

    public AuctionDeal(Long id, Long lotId, Long winningBidId, BigDecimal amount, String transferStatus) {
        this.id = id;
        this.lotId = lotId;
        this.winningBidId = winningBidId;
        this.amount = amount;
        this.transferStatus = transferStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId; }

    public Long getWinningBidId() { return winningBidId; }
    public void setWinningBidId(Long winningBidId) { this.winningBidId = winningBidId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransferStatus() { return transferStatus; }
    public void setTransferStatus(String transferStatus) { this.transferStatus = transferStatus; }
}