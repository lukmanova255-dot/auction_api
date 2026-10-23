package com.example.auction_api.service;

import com.example.auction_api.dto.UserRequest;
import com.example.auction_api.exception.NotFoundException;
import com.example.auction_api.model.User;
import com.example.auction_api.repository.CollectibleItemRepository;
import com.example.auction_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final CollectibleItemRepository itemRepository;

    public UserService(UserRepository repository, CollectibleItemRepository itemRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: id=" + id));
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public User create(UserRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        if (repository.existsByUsername(req.getUsername())) {
            throw new IllegalStateException("Username already taken: " + req.getUsername());
        }
        User user = new User(req.getUsername(), req.getEmail());
        return repository.save(user);
    }

    public User update(Long id, UserRequest req) {
        User user = findById(id);
        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            user.setUsername(req.getUsername());
        }
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            user.setEmail(req.getEmail());
        }
        return repository.save(user);
    }

    public void delete(Long id) {
        findById(id);
        if (itemRepository.existsByOwnerId(id)) {
            throw new IllegalStateException("Cannot delete User: there are CollectibleItems owned by this user");
        }
        repository.deleteById(id);
    }
}