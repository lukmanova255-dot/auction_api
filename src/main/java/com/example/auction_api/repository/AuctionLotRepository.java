package com.example.auction_api.repository;

import com.example.auction_api.model.AuctionLot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AuctionLotRepository {
    private final Map<Long, AuctionLot> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public List<AuctionLot> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<AuctionLot> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<AuctionLot> findByItemId(Long itemId) {
        List<AuctionLot> result = new ArrayList<>();
        for (AuctionLot lot : storage.values()) {
            if (itemId.equals(lot.getItemId())) {
                result.add(lot);
            }
        }
        return result;
    }

    public AuctionLot save(AuctionLot lot) {
        if (lot.getId() == null) {
            lot.setId(idSequence.getAndIncrement());
        }
        storage.put(lot.getId(), lot);
        return lot;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}