package com.watad.repo;


import com.watad.entity.Cart;
import com.watad.enumValues.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
