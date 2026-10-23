package com.example.auction_api.repository;

import com.example.auction_api.model.CollectibleItem;
import com.example.auction_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectibleItemRepository extends JpaRepository<CollectibleItem, Long> {
    List<CollectibleItem> findByOwner(User owner);
    List<CollectibleItem> findByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);
}