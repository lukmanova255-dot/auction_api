package com.example.auction_api.service;

import com.example.auction_api.dto.AuctionDealRequest;
import com.example.auction_api.exception.NotFoundException;
import com.example.auction_api.model.AuctionDeal;
import com.example.auction_api.model.AuctionLot;
import com.example.auction_api.model.Bid;
import com.example.auction_api.repository.AuctionDealRepository;
import com.example.auction_api.repository.AuctionLotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionDealService {

    private final AuctionDealRepository repository;
    private final AuctionLotService lotService;
    private final AuctionLotRepository lotRepository;
    private final BidService bidService;

    public AuctionDealService(AuctionDealRepository repository,
                              AuctionLotService lotService,
                              AuctionLotRepository lotRepository,
                              BidService bidService) {
        this.repository = repository;
        this.lotService = lotService;
        this.lotRepository = lotRepository;
        this.bidService = bidService;
    }

    public List<AuctionDeal> findAll() {
        return repository.findAll();
    }

    public AuctionDeal findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("AuctionDeal not found: id=" + id));
    }

    public AuctionDeal create(AuctionDealRequest req) {
        if (req.getLotId() == null) {
            throw new IllegalArgumentException("lotId is required");
        }

        AuctionLot lot = lotService.findById(req.getLotId());

        if (repository.findByLotId(req.getLotId()).isPresent()) {
            throw new IllegalStateException("Deal for this lot already exists");
        }

        List<Bid> bids = bidService.findAll().stream()
                .filter(b -> b.getLotId().equals(req.getLotId()))
                .toList();

        if (bids.isEmpty()) {
            throw new IllegalStateException("No bids for this lot");
        }

        Bid winner = bids.stream()
                .max((a, b) -> a.getAmount().compareTo(b.getAmount()))
                .orElseThrow();

        // Закрываем лот
        lot.setStatus("CLOSED");
        lotRepository.save(lot);

        AuctionDeal deal = new AuctionDeal(
                null,
                req.getLotId(),
                winner.getId(),
                winner.getAmount(),
                "PENDING"
        );
        return repository.save(deal);
    }

    public AuctionDeal update(Long id, AuctionDealRequest req) {
        AuctionDeal deal = findById(id);
        return repository.save(deal);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}