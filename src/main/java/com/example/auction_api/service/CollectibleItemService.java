package com.example.auction_api.service;

import com.example.auction_api.dto.CollectibleItemRequest;
import com.example.auction_api.exception.NotFoundException;
import com.example.auction_api.model.CollectibleItem;
import com.example.auction_api.model.User;
import com.example.auction_api.repository.AuctionLotRepository;
import com.example.auction_api.repository.CollectibleItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectibleItemService {

    private final CollectibleItemRepository repository;
    private final UserService userService;
    private final AuctionLotRepository lotRepository;

    public CollectibleItemService(CollectibleItemRepository repository,
                                  UserService userService,
                                  AuctionLotRepository lotRepository) {
        this.repository = repository;
        this.userService = userService;
        this.lotRepository = lotRepository;
    }

    public List<CollectibleItem> findAll() {
        return repository.findAll();
    }

    public CollectibleItem findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("CollectibleItem not found: id=" + id));
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public CollectibleItem create(CollectibleItemRequest req) {
        if (req.getName() == null || req.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (req.getCondition() == null || req.getCondition().isBlank()) {
            throw new IllegalArgumentException("condition is required");
        }
        if (req.getOwnerId() == null) {
            throw new IllegalArgumentException("ownerId is required");
        }
        User owner = userService.findById(req.getOwnerId());

        CollectibleItem item = new CollectibleItem(
                req.getName(),
                req.getDescription(),
                req.getCondition(),
                owner
        );
        return repository.save(item);
    }

    public CollectibleItem update(Long id, CollectibleItemRequest req) {
        CollectibleItem item = findById(id);
        if (req.getName() != null && !req.getName().isBlank()) {
            item.setName(req.getName());
        }
        if (req.getDescription() != null) {
            item.setDescription(req.getDescription());
        }
        if (req.getCondition() != null && !req.getCondition().isBlank()) {
            item.setCondition(req.getCondition());
        }
        if (req.getOwnerId() != null) {
            User owner = userService.findById(req.getOwnerId());
            item.setOwner(owner);
        }
        return repository.save(item);
    }

    public void delete(Long id) {
        findById(id);
        if (lotRepository.existsByItemId(id)) {
            throw new IllegalStateException("Cannot delete CollectibleItem: there are AuctionLots referencing it");
        }
        repository.deleteById(id);
    }
}