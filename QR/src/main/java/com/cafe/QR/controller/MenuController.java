package com.cafe.QR.controller;

import com.cafe.QR.entity.MenuItem;
import com.cafe.QR.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * Public endpoint for customers to view available menu items.
     */
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenu() {
        return ResponseEntity.ok(menuService.getAvailableMenu());
    }

    /**
     * SECURED endpoint for admins (staff/owner) to view ALL menu items.
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    /**
     * SECURED endpoint for admins (staff/owner) to toggle an item's availability.
     */
    @PutMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<?> toggleAvailability(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(menuService.toggleMenuItemAvailability(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * SECURED endpoint to add a new menu item. OWNER ONLY.
     */
    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.addMenuItem(menuItem));
    }

    /**
     * SECURED endpoint to update an existing menu item. OWNER ONLY.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        try {
            return ResponseEntity.ok(menuService.updateMenuItem(id, menuItem));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * SECURED endpoint to delete a menu item. OWNER ONLY.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        try {
            menuService.deleteMenuItemById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}