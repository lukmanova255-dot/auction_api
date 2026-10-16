package com.example.auction_api.controller;

import com.example.auction_api.dto.BidRequest;
import com.example.auction_api.dto.BidResponse;
import com.example.auction_api.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService service;

    public BidController(BidService service) {
        this.service = service;
    }

    @GetMapping
    public List<BidResponse> findAll() {
        return service.findAll().stream().map(BidResponse::from).toList();
    }

    @GetMapping("/{id}")
    public BidResponse findById(@PathVariable Long id) {
        return BidResponse.from(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BidResponse create(@RequestBody BidRequest req) {
        return BidResponse.from(service.create(req));
    }

    @PatchMapping("/{id}")
    public BidResponse update(@PathVariable Long id, @RequestBody BidRequest req) {
        return BidResponse.from(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}