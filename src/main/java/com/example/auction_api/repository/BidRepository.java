package com.example.auction_api.repository;

import com.example.auction_api.model.AuctionLot;
import com.example.auction_api.model.Bid;
import com.example.auction_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByLot(AuctionLot lot);
    List<Bid> findByLotId(Long lotId);
    List<Bid> findByUser(User user);
    Optional<Bid> findTopByLotIdOrderByAmountDesc(Long lotId);
    boolean existsByLotId(Long lotId);
}