package com.watad.repo;


import com.watad.dto.marketDashboard.ByItemDto;
import com.watad.dto.marketDashboard.BySupplierDto;
import com.watad.dto.marketDashboard.ByUserDto;
import com.watad.entity.Cart;
import com.watad.enumValues.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query("""
       SELECT COALESCE(SUM(c.itemCount), 0)
       FROM Cart c
       WHERE c.sprint.id = :sprintId
       AND c.user.id = :userId
       AND c.status = :status
       """)
    Long countItemsBySprintAndUserAndStatus(@Param("sprintId") int sprintId,
                                   @Param("userId") int userId,
                                   @Param("status") CartStatus cartStatus);

    @Query("""
       SELECT COALESCE(SUM(c.itemCount), 0)
       FROM Cart c
       WHERE c.sprint.id = :sprintId
       AND c.user.id = :userId
       """)
    Long countItemsBySprintAndUser(@Param("sprintId") int sprintId,
                                   @Param("userId") int userId);

    List<Cart> findByUserIdAndSprintIdAndStatus(int userId, int sprintId, CartStatus status);
    List<Cart> findByUserIdAndSprintId(int userId, int sprintId);


    @Query("""
            SELECT c.marketItem.itemName      AS itemName,
                   c.marketItem.supplierName  AS supplierName,
                   SUM(c.itemCount)           AS totalSold,
                   SUM(c.waznaPoints)         AS totalPoints
            FROM Cart c
            WHERE c.sprint.id = :sprintId
              AND c.status != com.watad.enumValues.CartStatus.CANCELLED
            GROUP BY c.marketItem.itemName, c.marketItem.supplierName
        """)
    List<ByItemDto> findByItem(@Param("sprintId") int sprintId);

    @Query("""
    SELECT c.marketItem.supplierName       AS supplierName,
           COUNT(DISTINCT c.marketItem.id) AS totalItems,
           SUM(c.itemCount)                AS totalSold,
           SUM(c.waznaPoints)              AS totalPoints
    FROM Cart c
    WHERE c.sprint.id = :sprintId
      AND c.status != com.watad.enumValues.CartStatus.CANCELLED
    GROUP BY c.marketItem.supplierName
""")
    List<BySupplierDto> findBySupplier(@Param("sprintId") int sprintId);


    @Query("""
    SELECT c.id                              AS cartId,
           c.user.profile.firstName          AS firstName,
           c.user.profile.lastName           AS lastName,
           c.marketItem.itemName             AS itemName,
           c.marketItem.supplierName         AS supplierName,
           c.itemCount                       AS itemCount,
           c.waznaPoints                     AS points,
           c.PurchaseDate                    AS purchaseDate,
           c.status                          AS status
    FROM Cart c
    WHERE c.sprint.id = :sprintId
    ORDER BY c.PurchaseDate DESC
""")
    List<ByUserDto> findByUser(@Param("sprintId") int sprintId);

    @Modifying
    @Query("""
        update Cart c
        set c.status = :status
        where c.id = :cartId
       """)
    void changeStatusOfCartItem(@Param("status") CartStatus cartStatus,
                                @Param("cartId") int cartId);
}
