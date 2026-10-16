package com.example.auction_api.service;

import com.example.auction_api.dto.AuctionLotRequest;
import com.example.auction_api.exception.NotFoundException;
import com.example.auction_api.model.AuctionLot;
import com.example.auction_api.repository.AuctionDealRepository;
import com.example.auction_api.repository.AuctionLotRepository;
import com.example.auction_api.repository.BidRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionLotService {

    private final AuctionLotRepository repository;
    private final CollectibleItemService itemService;
    private final BidRepository bidRepository;
    private final AuctionDealRepository dealRepository;

    public AuctionLotService(AuctionLotRepository repository,
                             CollectibleItemService itemService,
                             BidRepository bidRepository,
                             AuctionDealRepository dealRepository) {
        this.repository = repository;
        this.itemService = itemService;
        this.bidRepository = bidRepository;
        this.dealRepository = dealRepository;
    }

    public List<AuctionLot> findAll() {
        return repository.findAll();
    }

    public AuctionLot findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("AuctionLot not found: id=" + id));
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public AuctionLot create(AuctionLotRequest req) {
        if (req.getItemId() == null) {
            throw new IllegalArgumentException("itemId is required");
        }
        if (!itemService.existsById(req.getItemId())) {
            throw new NotFoundException("CollectibleItem not found: id=" + req.getItemId());
        }
        if (req.getStartPrice() == null || req.getStartPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("startPrice must be > 0");
        }
        if (req.getBidStep() == null || req.getBidStep().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("bidStep must be > 0");
        }

        LocalDateTime now = LocalDateTime.now();
        AuctionLot lot = new AuctionLot(
                null,
                req.getItemId(),
                req.getStartPrice(),
                req.getBidStep(),
                now,
                now.plusDays(7),
                "OPEN"
        );
        return repository.save(lot);
    }

    public AuctionLot update(Long id, AuctionLotRequest req) {
        AuctionLot lot = findById(id);
        if ("CLOSED".equals(lot.getStatus())) {
            throw new IllegalStateException("AuctionLot is closed and cannot be modified");
        }
        if (req.getStartPrice() != null && req.getStartPrice().compareTo(BigDecimal.ZERO) > 0) {
            lot.setStartPrice(req.getStartPrice());
        }
        if (req.getBidStep() != null && req.getBidStep().compareTo(BigDecimal.ZERO) > 0) {
            lot.setBidStep(req.getBidStep());
        }
        return repository.save(lot);
    }

    public AuctionLot save(AuctionLot lot) {
        return repository.save(lot);
    }

    public void delete(Long id) {
        findById(id);
        if (dealRepository.findByLotId(id).isPresent()) {
            throw new IllegalStateException("Cannot delete AuctionLot: deal already exists");
        }
        // Каскадно удаляем все ставки, чтобы не осталось висячих ссылок
        List<Long> bidIds = bidRepository.findByLotId(id).stream().map(b -> b.getId()).toList();
        for (Long bidId : bidIds) {
            bidRepository.deleteById(bidId);
        }
        repository.deleteById(id);
    }
}