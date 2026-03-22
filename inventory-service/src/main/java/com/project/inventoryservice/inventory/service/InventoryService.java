package com.project.inventoryservice.inventory.service;

import com.project.inventoryservice.inventory.entity.Inventory;
import com.project.inventoryservice.inventory.repo.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // Stock add karo
    public Inventory addStock(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // Stock check karo
    public boolean isStockAvailable(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return inventory.getQuantity() >= quantity;
    }

    // Stock reduce karo
    public Inventory reduceStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        return inventoryRepository.save(inventory);
    }

    // Stock get karo
    public Inventory getStock(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Sab products dekho
    public List<Inventory> getAllStock() {
        return inventoryRepository.findAll();
    }
}