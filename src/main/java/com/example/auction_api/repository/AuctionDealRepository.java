package com.example.auction_api.repository;

import com.example.auction_api.model.AuctionDeal;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AuctionDealRepository {
    private final Map<Long, AuctionDeal> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public List<AuctionDeal> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<AuctionDeal> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<AuctionDeal> findByLotId(Long lotId) {
        for (AuctionDeal deal : storage.values()) {
            if (lotId.equals(deal.getLotId())) {
                return Optional.of(deal);
            }
        }
        return Optional.empty();
    }

    public AuctionDeal save(AuctionDeal deal) {
        if (deal.getId() == null) {
            deal.setId(idSequence.getAndIncrement());
        }
        storage.put(deal.getId(), deal);
        return deal;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}