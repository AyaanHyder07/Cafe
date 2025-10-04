package com.cafe.QR.controller;

import com.cafe.QR.entity.MenuItem;
import com.cafe.QR.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenu() {
        return ResponseEntity.ok(menuService.getAvailableMenu());
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<?> toggleAvailability(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(menuService.toggleMenuItemAvailability(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.addMenuItem(menuItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        try {
            return ResponseEntity.ok(menuService.updateMenuItem(id, menuItem));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        try {
            menuService.deleteMenuItemById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}