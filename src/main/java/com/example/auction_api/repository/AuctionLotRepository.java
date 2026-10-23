package com.example.auction_api.repository;

import com.example.auction_api.model.AuctionLot;
import com.example.auction_api.model.CollectibleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionLotRepository extends JpaRepository<AuctionLot, Long> {
    List<AuctionLot> findByItem(CollectibleItem item);
    List<AuctionLot> findByItemId(Long itemId);
    boolean existsByItemId(Long itemId);
    Optional<AuctionLot> findByIdAndStatus(Long id, String status);
    List<AuctionLot> findByItemIdAndStatus(Long itemId, String status);

}