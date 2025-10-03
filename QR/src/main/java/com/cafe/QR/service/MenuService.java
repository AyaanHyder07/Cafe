package com.cafe.QR.service;

import com.cafe.QR.entity.MenuItem;
import com.cafe.QR.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    // For customers
    public List<MenuItem> getAvailableMenu() {
        return menuItemRepository.findByIsAvailableTrue();
    }

    // For admins (staff & owner)
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // For admins (staff & owner)
    public MenuItem toggleMenuItemAvailability(Long itemId) {
        MenuItem menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Menu item not found with ID: " + itemId));
        
        menuItem.setAvailable(!menuItem.isAvailable());
        
        return menuItemRepository.save(menuItem);
    }
    
    // --- NEW METHODS FOR OWNER ONLY ---

    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(Long itemId, MenuItem updatedItemDetails) {
        MenuItem menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Menu item not found with ID: " + itemId));

        menuItem.setName(updatedItemDetails.getName());
        menuItem.setDescription(updatedItemDetails.getDescription());
        menuItem.setPrice(updatedItemDetails.getPrice());
        menuItem.setCategory(updatedItemDetails.getCategory());
        
        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItemById(Long itemId) {
        if (!menuItemRepository.existsById(itemId)) {
            throw new NoSuchElementException("Menu item not found with ID: " + itemId);
        }
        menuItemRepository.deleteById(itemId);
    }
}