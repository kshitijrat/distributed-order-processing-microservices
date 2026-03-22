package com.project.inventoryservice.inventory.controller;

import com.project.inventoryservice.inventory.entity.Inventory;
import com.project.inventoryservice.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Naya stock add karo
    @PostMapping("/add")
    public ResponseEntity<Inventory> addStock(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.addStock(inventory));
    }

    // Stock check karo
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkStock(@RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.isStockAvailable(productId, quantity));
    }

    // Stock reduce karo
    @PutMapping("/reduce")
    public ResponseEntity<Inventory> reduceStock(@RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.reduceStock(productId, quantity));
    }

    // Product ka stock dekho
    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }

    // Sab products ka stock dekho
    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> getAllStock() {
        return ResponseEntity.ok(inventoryService.getAllStock());
    }
}