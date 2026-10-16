package com.example.auction_api.controller;

import com.example.auction_api.dto.AuctionDealRequest;
import com.example.auction_api.dto.AuctionDealResponse;
import com.example.auction_api.service.AuctionDealService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auction-deals")
public class AuctionDealController {

    private final AuctionDealService service;

    public AuctionDealController(AuctionDealService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuctionDealResponse> findAll() {
        return service.findAll().stream().map(AuctionDealResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AuctionDealResponse findById(@PathVariable Long id) {
        return AuctionDealResponse.from(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuctionDealResponse create(@RequestBody AuctionDealRequest req) {
        return AuctionDealResponse.from(service.create(req));
    }

    @PatchMapping("/{id}")
    public AuctionDealResponse update(@PathVariable Long id, @RequestBody AuctionDealRequest req) {
        return AuctionDealResponse.from(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}