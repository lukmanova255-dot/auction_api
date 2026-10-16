package com.example.auction_api.controller;

import com.example.auction_api.dto.AuctionLotRequest;
import com.example.auction_api.dto.AuctionLotResponse;
import com.example.auction_api.service.AuctionLotService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auction-lots")
public class AuctionLotController {

    private final AuctionLotService service;

    public AuctionLotController(AuctionLotService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuctionLotResponse> findAll() {
        return service.findAll().stream().map(AuctionLotResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AuctionLotResponse findById(@PathVariable Long id) {
        return AuctionLotResponse.from(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuctionLotResponse create(@RequestBody AuctionLotRequest req) {
        return AuctionLotResponse.from(service.create(req));
    }

    @PatchMapping("/{id}")
    public AuctionLotResponse update(@PathVariable Long id, @RequestBody AuctionLotRequest req) {
        return AuctionLotResponse.from(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}