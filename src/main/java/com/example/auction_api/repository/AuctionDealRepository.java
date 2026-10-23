package com.example.auction_api.repository;

import com.example.auction_api.model.AuctionDeal;
import com.example.auction_api.model.AuctionLot;
import com.example.auction_api.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionDealRepository extends JpaRepository<AuctionDeal, Long> {
    Optional<AuctionDeal> findByLot(AuctionLot lot);
    Optional<AuctionDeal> findByLotId(Long lotId);
    boolean existsByWinningBid(Bid bid);
    boolean existsByWinningBidId(Long bidId);
}