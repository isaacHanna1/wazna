package com.watad.dao;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.UserPointTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserPointTransactionDaoImp implements  UserPointTransactionDao{

    private final EntityManager entityManager;

    public UserPointTransactionDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(UserPointTransaction userPointTransaction) {
        entityManager.persist(userPointTransaction);
    }

    @Override
    public double getTotalPointsByProfileIdAndSprintId(int profileId, Integer sprintId) {
        String query = "SELECT SUM(u.points) FROM UserPointTransaction u WHERE u.profile.id = :profileId AND u.sprintData.id = :sprintId AND u.isActive = true";
        TypedQuery<Double> typedQuery = entityManager.createQuery(query, Double.class);
        typedQuery.setParameter("profileId", profileId);
        typedQuery.setParameter("sprintId", sprintId);
        Double singleResult = (typedQuery.getSingleResult() == null)?0:typedQuery.getSingleResult();
        return  singleResult;
    }

    @Override
    public List<ProfileDtlDto> findProfileBuUserName(int church_id, int meeting_id, int sprint_id, String userPhone , int userRole) {
        System.out.println("the church id "+church_id +"the meeting_id "+meeting_id+" the sprint id "+sprint_id +" the user phone :"+userPhone);
        List <ProfileDtlDto> listOfProfile = new ArrayList<>();
        String sql = """
                               SELECT
                                   p.profile_id,
                                   p.first_name,
                                   p.last_name,
                                   p.phone,
                                   COALESCE(SUM(upt.points), 0) AS total_points,
                                   p.church_id,
                                   p.meeting_id
                               FROM
                                   profile p
                               LEFT JOIN
                                   user_point_transaction upt
                                   ON p.profile_id    = upt.profile_id
                                   AND upt.church_id  =:church_id
                                   AND upt.meeting_id =:meeting_id
                                   AND upt.sprint_id  =:sprint_id
                               JOIN user u ON u.profile_id = p.profile_id 
                               WHERE
                                   p.church_id      = :church_id
                                   AND p.meeting_id = :meeting_id
                                   AND p.phone LIKE CONCAT('%', :userPhone, '%')
                                   AND u.id IN (
                       					 SELECT ur.user_id
                       					 FROM user_role ur
                       					 WHERE ur.role_id <= :userRole
                                    )
                               GROUP BY
                                   p.profile_id,
                                   p.first_name,
                                   p.last_name,
                                   p.phone,
                                   p.church_id,
                                   p.meeting_id
                            """;
        List<Object[]> result = entityManager.createNativeQuery(sql)
                .setParameter("sprint_id",sprint_id)
                .setParameter("church_id",church_id)
                .setParameter("userPhone",userPhone)
                .setParameter("userRole",userRole)
                .setParameter("meeting_id",meeting_id).getResultList();

        for (Object[] row : result) {
            ProfileDtlDto p   = new ProfileDtlDto();
            int profileId     =   ((Number) row[0]).intValue();
            String firstName  =   (String) row[1];
            String lastName   =   (String) row[2];
            String phone      =   (String) row[3];
            double point      =   ((Double) row[4]).doubleValue();
            p.setId(profileId);
            p.setFirstName(firstName);
            p.setLastName(lastName);
            p.setPoints(point);
            p.setPhone(phone);
            listOfProfile.add(p);
        }
        return listOfProfile;

    }
}
