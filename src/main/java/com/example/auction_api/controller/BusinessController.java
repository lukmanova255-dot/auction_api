package com.example.auction_api.controller;

import com.example.auction_api.dto.AuctionDealResponse;
import com.example.auction_api.dto.CollectibleItemResponse;
import com.example.auction_api.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    private final BusinessService service;

    public BusinessController(BusinessService service) {
        this.service = service;
    }

    @PostMapping("/lots/{lotId}/close")
    public AuctionDealResponse closeLot(@PathVariable Long lotId) {
        return AuctionDealResponse.from(service.closeLotAndCreateDeal(lotId));
    }

    @PostMapping("/deals/{dealId}/pay")
    public AuctionDealResponse payDeal(@PathVariable Long dealId) {
        return AuctionDealResponse.from(service.payDeal(dealId));
    }

    @PostMapping("/bids/{bidId}/withdraw")
    public ResponseEntity<Void> withdrawBid(@PathVariable Long bidId, @RequestParam Long userId) {
        service.withdrawBid(bidId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lots/{lotId}/cancel")
    public ResponseEntity<Void> cancelLot(@PathVariable Long lotId) {
        service.cancelLot(lotId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/items/{itemId}/transfer")
    public CollectibleItemResponse transferItem(@PathVariable Long itemId, @RequestParam Long newOwnerId) {
        return CollectibleItemResponse.from(service.transferItem(itemId, newOwnerId));
    }
}