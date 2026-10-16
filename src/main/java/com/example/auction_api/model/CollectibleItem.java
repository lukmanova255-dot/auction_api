package com.example.auction_api.model;

public class CollectibleItem {
    private Long id;
    private String name;
    private String description;
    private String condition;
    private Long ownerId;

    public CollectibleItem(Long id, String name, String description, String condition, Long ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.ownerId = ownerId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}