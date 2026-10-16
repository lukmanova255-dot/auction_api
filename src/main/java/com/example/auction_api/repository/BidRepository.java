package com.example.auction_api.repository;

import com.example.auction_api.model.Bid;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BidRepository {
    private final Map<Long, Bid> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public List<Bid> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Bid> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Bid> findByLotId(Long lotId) {
        List<Bid> result = new ArrayList<>();
        for (Bid bid : storage.values()) {
            if (lotId.equals(bid.getLotId())) {
                result.add(bid);
            }
        }
        return result;
    }

    public Bid save(Bid bid) {
        if (bid.getId() == null) {
            bid.setId(idSequence.getAndIncrement());
        }
        storage.put(bid.getId(), bid);
        return bid;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}