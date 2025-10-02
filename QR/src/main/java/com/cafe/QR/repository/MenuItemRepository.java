package com.cafe.QR.repository;

import com.cafe.QR.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // Custom query to find all menu items that are currently available
    List<MenuItem> findByIsAvailableTrue();
}