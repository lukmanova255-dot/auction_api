package com.example.auction_api.dto;

import com.example.auction_api.model.CollectibleItem;

public class CollectibleItemResponse {
    private Long id;
    private String name;
    private String description;
    private String condition;
    private Long ownerId;

    public CollectibleItemResponse(Long id, String name, String description, String condition, Long ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.ownerId = ownerId;
    }

    public static CollectibleItemResponse from(CollectibleItem item) {
        return new CollectibleItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getCondition(),
                item.getOwner() != null ? item.getOwner().getId() : null
        );
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCondition() { return condition; }
    public Long getOwnerId() { return ownerId; }
}