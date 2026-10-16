package com.example.auction_api.repository;

import com.example.auction_api.model.CollectibleItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CollectibleItemRepository {
    private final Map<Long, CollectibleItem> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public List<CollectibleItem> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<CollectibleItem> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<CollectibleItem> findByOwnerId(Long ownerId) {
        List<CollectibleItem> result = new ArrayList<>();
        for (CollectibleItem item : storage.values()) {
            if (ownerId.equals(item.getOwnerId())) {
                result.add(item);
            }
        }
        return result;
    }

    public CollectibleItem save(CollectibleItem item) {
        if (item.getId() == null) {
            item.setId(idSequence.getAndIncrement());
        }
        storage.put(item.getId(), item);
        return item;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}