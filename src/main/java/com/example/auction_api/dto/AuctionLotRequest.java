package com.example.auction_api.dto;

import java.math.BigDecimal;

public class AuctionLotRequest {
    private Long itemId;
    private BigDecimal startPrice;
    private BigDecimal bidStep;

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public BigDecimal getStartPrice() { return startPrice; }
    public void setStartPrice(BigDecimal startPrice) { this.startPrice = startPrice; }

    public BigDecimal getBidStep() { return bidStep; }
    public void setBidStep(BigDecimal bidStep) { this.bidStep = bidStep; }
}