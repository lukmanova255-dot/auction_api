package com.example.auction_api.service;

import com.example.auction_api.exception.NotFoundException;
import com.example.auction_api.model.*;
import com.example.auction_api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BusinessService {

    private final AuctionLotService lotService;
    private final AuctionLotRepository lotRepository;
    private final BidService bidService;
    private final BidRepository bidRepository;
    private final AuctionDealService dealService;
    private final AuctionDealRepository dealRepository;
    private final CollectibleItemService itemService;
    private final CollectibleItemRepository itemRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public BusinessService(AuctionLotService lotService, AuctionLotRepository lotRepository,
                           BidService bidService, BidRepository bidRepository,
                           AuctionDealService dealService, AuctionDealRepository dealRepository,
                           CollectibleItemService itemService, CollectibleItemRepository itemRepository,
                           UserService userService, UserRepository userRepository) {
        this.lotService = lotService;
        this.lotRepository = lotRepository;
        this.bidService = bidService;
        this.bidRepository = bidRepository;
        this.dealService = dealService;
        this.dealRepository = dealRepository;
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * 1. Закрыть лот и создать сделку.
     * Условия: лот OPEN, endTime прошло ИЛИ принудительно, есть ставки.
     * Затрагивает: AuctionLot, Bid, AuctionDeal.
     */
    @Transactional
    public AuctionDeal closeLotAndCreateDeal(Long lotId) {
        AuctionLot lot = lotService.findById(lotId);
        if (!"OPEN".equals(lot.getStatus())) {
            throw new IllegalStateException("Lot is not OPEN");
        }
        Bid winner = bidRepository.findTopByLotIdOrderByAmountDesc(lotId)
                .orElseThrow(() -> new IllegalStateException("No bids for this lot"));

        lot.setStatus("CLOSED");
        lotRepository.save(lot);

        AuctionDeal deal = new AuctionDeal(lot, winner, winner.getAmount(), "PENDING");
        return dealRepository.save(deal);
    }

    /**
     * 2. Оплатить сделку.
     * Условия: сделка PENDING.
     * Затрагивает: AuctionDeal, CollectibleItem (меняет владельца).
     */
    @Transactional
    public AuctionDeal payDeal(Long dealId) {
        AuctionDeal deal = dealService.findById(dealId);
        if (!"PENDING".equals(deal.getTransferStatus())) {
            throw new IllegalStateException("Deal is not in PENDING status");
        }

        CollectibleItem item = deal.getLot().getItem();
        User winner = deal.getWinningBid().getUser();
        item.setOwner(winner);
        itemRepository.save(item);

        deal.setTransferStatus("PAID");
        return dealRepository.save(deal);
    }

    /**
     * 3. Отозвать ставку пользователя.
     * Условия: ставка не выигрышная (лот OPEN), пользователь — автор ставки.
     * Затрагивает: Bid, AuctionLot (проверка статуса).
     */
    @Transactional
    public void withdrawBid(Long bidId, Long userId) {
        Bid bid = bidService.findById(bidId);
        if (!bid.getUser().getId().equals(userId)) {
            throw new IllegalStateException("User is not the owner of this bid");
        }
        AuctionLot lot = bid.getLot();
        if (!"OPEN".equals(lot.getStatus())) {
            throw new IllegalStateException("Cannot withdraw bid on a closed lot");
        }
        bidRepository.delete(bid);
    }

    /**
     * 4. Отменить лот.
     * Условия: лот OPEN, НЕТ ставок.
     * Затрагивает: AuctionLot, Bid (проверка отсутствия).
     */
    @Transactional
    public void cancelLot(Long lotId) {
        AuctionLot lot = lotService.findById(lotId);
        if (!"OPEN".equals(lot.getStatus())) {
            throw new IllegalStateException("Lot is not OPEN");
        }
        if (bidRepository.existsByLotId(lotId)) {
            throw new IllegalStateException("Cannot cancel lot with existing bids");
        }
        lot.setStatus("CANCELLED");
        lotRepository.save(lot);
    }

    /**
     * 5. Передать предмет другому пользователю.
     * Условия: предмет не участвует в активном лоте (OPEN).
     * Затрагивает: CollectibleItem, User, AuctionLot (проверка).
     */
    @Transactional
    public CollectibleItem transferItem(Long itemId, Long newOwnerId) {
        CollectibleItem item = itemService.findById(itemId);
        User newOwner = userService.findById(newOwnerId);
        if (!lotRepository.findByItemIdAndStatus(itemId, "OPEN").isEmpty()) {
            throw new IllegalStateException("Cannot transfer item: it is in an active lot");
        }
        item.setOwner(newOwner);
        return itemRepository.save(item);
    }
}