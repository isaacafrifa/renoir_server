package com.iam.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/* NOTE: be very careful about ALLOW FILTERING in real world apps, this
 may affect scalability quite a lot. Filtering is efficient over primary
 keys, not on all generic columns
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {

    boolean existsByName(String menuName);
}
