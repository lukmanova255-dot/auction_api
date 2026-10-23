package com.example.auction_api.service;

import com.example.auction_api.dto.BidRequest;
import com.example.auction_api.exception.NotFoundException;
import com.example.auction_api.model.AuctionLot;
import com.example.auction_api.model.Bid;
import com.example.auction_api.model.User;
import com.example.auction_api.repository.AuctionDealRepository;
import com.example.auction_api.repository.BidRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    private final BidRepository repository;
    private final AuctionLotService lotService;
    private final UserService userService;
    private final AuctionDealRepository dealRepository;

    public BidService(BidRepository repository,
                      AuctionLotService lotService,
                      UserService userService,
                      AuctionDealRepository dealRepository) {
        this.repository = repository;
        this.lotService = lotService;
        this.userService = userService;
        this.dealRepository = dealRepository;
    }

    public List<Bid> findAll() {
        return repository.findAll();
    }

    public Bid findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bid not found: id=" + id));
    }

    public Bid create(BidRequest req) {
        if (req.getLotId() == null) {
            throw new IllegalArgumentException("lotId is required");
        }
        if (req.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }

        AuctionLot lot = lotService.findById(req.getLotId());
        if ("CLOSED".equals(lot.getStatus())) {
            throw new IllegalStateException("AuctionLot is closed");
        }

        User user = userService.findById(req.getUserId());

        BigDecimal minAmount = lot.getStartPrice().add(lot.getBidStep());
        if (req.getAmount().compareTo(minAmount) < 0) {
            throw new IllegalArgumentException("amount must be >= " + minAmount);
        }

        Bid bid = new Bid(lot, user, req.getAmount(), LocalDateTime.now());
        return repository.save(bid);
    }

    public Bid update(Long id, BidRequest req) {
        Bid bid = findById(id);
        if (req.getAmount() != null && req.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            bid.setAmount(req.getAmount());
        }
        return repository.save(bid);
    }

    public void delete(Long id) {
        findById(id);
        if (dealRepository.existsByWinningBidId(id)) {
            throw new IllegalStateException("Cannot delete Bid: it is the winning bid of an AuctionDeal");
        }
        repository.deleteById(id);
    }
}