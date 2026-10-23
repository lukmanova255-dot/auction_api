package com.example.auction_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "auction_deals")
public class AuctionDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lot_id", nullable = false, unique = true)
    private AuctionLot lot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winning_bid_id", nullable = false, unique = true)
    private Bid winningBid;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "transfer_status", nullable = false, length = 30)
    private String transferStatus;

    public AuctionDeal() {}

    public AuctionDeal(AuctionLot lot, Bid winningBid, BigDecimal amount, String transferStatus) {
        this.lot = lot;
        this.winningBid = winningBid;
        this.amount = amount;
        this.transferStatus = transferStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AuctionLot getLot() { return lot; }
    public void setLot(AuctionLot lot) { this.lot = lot; }

    public Bid getWinningBid() { return winningBid; }
    public void setWinningBid(Bid winningBid) { this.winningBid = winningBid; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransferStatus() { return transferStatus; }
    public void setTransferStatus(String transferStatus) { this.transferStatus = transferStatus; }
}