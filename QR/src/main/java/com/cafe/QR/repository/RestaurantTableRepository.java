package com.cafe.QR.repository;

import com.cafe.QR.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    // Custom query to find a table by its unique QR code identifier
    Optional<RestaurantTable> findByQrCode(String qrCode);
}