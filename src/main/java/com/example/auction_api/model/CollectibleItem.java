package com.example.auction_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "collectible_items")
public class CollectibleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String condition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public CollectibleItem() {}

    public CollectibleItem(String name, String description, String condition, User owner) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}