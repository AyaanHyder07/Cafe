package com.cafe.QR.service;

import com.cafe.QR.entity.MenuItem;
import com.cafe.QR.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    /**
     * Business Logic: Fetches only the menu items marked as available.
     * This ensures customers don't see or order items that are out of stock.
     * This uses the custom repository method we defined.
     *
     * @return A list of available menu items.
     */
    public List<MenuItem> getAvailableMenu() {
        return menuItemRepository.findByIsAvailableTrue();
    }

    /**
     * Business Logic: Toggles the availability status of a menu item.
     * This is a common real-world operation for an admin dashboard (e.g., "86 an item").
     *
     * @param itemId The ID of the menu item to update.
     * @return The updated MenuItem entity.
     */
    public MenuItem toggleMenuItemAvailability(Long itemId) {
        MenuItem menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Menu item not found with ID: " + itemId));
        
        menuItem.setAvailable(!menuItem.isAvailable()); // Flip the boolean status
        
        return menuItemRepository.save(menuItem);
    }
    
    // You would also have standard CRUD-like services for admin management
    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }
}