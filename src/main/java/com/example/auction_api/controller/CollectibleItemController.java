package com.example.auction_api.controller;

import com.example.auction_api.dto.CollectibleItemRequest;
import com.example.auction_api.dto.CollectibleItemResponse;
import com.example.auction_api.service.CollectibleItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collectible-items")
public class CollectibleItemController {

    private final CollectibleItemService service;

    public CollectibleItemController(CollectibleItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<CollectibleItemResponse> findAll() {
        return service.findAll().stream().map(CollectibleItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CollectibleItemResponse findById(@PathVariable Long id) {
        return CollectibleItemResponse.from(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollectibleItemResponse create(@RequestBody CollectibleItemRequest req) {
        return CollectibleItemResponse.from(service.create(req));
    }

    @PatchMapping("/{id}")
    public CollectibleItemResponse update(@PathVariable Long id, @RequestBody CollectibleItemRequest req) {
        return CollectibleItemResponse.from(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}