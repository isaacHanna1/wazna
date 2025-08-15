package com.watad.dao;

import com.watad.dto.PointTransactionSummaryDto;
import com.watad.dto.PointsSummaryDTO;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.UserPointTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public List<PointTransactionSummaryDto> getSummaryOfPoints(int profileId, int sprintId, int churchId, int meetingId) {
        List <PointTransactionSummaryDto> listOfTranactions= new ArrayList<>();
            String sql = """
                    SELECT
                        t.transaction_date,
                        t.transaction_type,
                        t.used_for,
                        t.points AS s,
                        SUM(t.points) OVER (
                            ORDER BY t.transaction_date, t.transaction_id
                            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                        ) AS current_sum,
                        SUM(t.points) OVER (
                            ORDER BY t.transaction_date, t.transaction_id
                            ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING
                        ) AS 'before'
                    FROM wazna.user_point_transaction t
                    WHERE t.profile_id = :profileId
                      AND t.sprint_id =  :sprintId
                      AND t.church_id = :churchId
                      AND t.meeting_id = :meetingId
                    ORDER BY t.transaction_date, t.transaction_id;
                    
                                """;

        System.out.println("profileId = " + profileId);
        System.out.println("sprintId = " + sprintId);
        System.out.println("churchId = " + churchId);
        System.out.println("meetingId = " + meetingId);
            List<Object[]> result = entityManager.createNativeQuery(sql)
                    .setParameter("profileId",profileId)
                    .setParameter("sprintId",sprintId)
                    .setParameter("churchId",churchId)
                    .setParameter("meetingId",meetingId).getResultList();

            System.out.println("the result size is "+result.size());
            for (Object[] row : result) {
                LocalDateTime transactionTime     =   ((Timestamp) row[0]).toLocalDateTime();
                String transactionType  =   (String) row[1];
                String usedFor          =   (String) row[2];
                double point                  = row[3] == null ? 0 : ((Double) row[3]).doubleValue();
                double currentSum             = row[4] == null ? 0 : ((Double) row[4]).doubleValue();
                double before                 = row[5] == null ? 0 : ((Double) row[5]).doubleValue();
                PointTransactionSummaryDto p   = new PointTransactionSummaryDto(transactionTime,transactionType,usedFor,point,currentSum,before);
                listOfTranactions.add(p);
            }
            return listOfTranactions;
    }

}
