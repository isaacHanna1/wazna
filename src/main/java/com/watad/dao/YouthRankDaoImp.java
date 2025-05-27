package com.watad.dao;

import com.watad.dto.response.YouthRankDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class YouthRankDaoImp implements YouthRankDao  {

    private final EntityManager entityManager;

    public YouthRankDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<YouthRankDto> getYouthRank(int sprintId, int churchId, int meetingId , int limit, int offset) {
        String nativeSql = """
        SELECT 
           DENSE_RANK() OVER (PARTITION BY p.meeting_id, p.church_id , upt.sprint_id ORDER BY SUM(upt.points) DESC) AS r,
            p.profile_id,
            p.first_name,
            p.last_name,
            SUM(upt.points) AS total_points
        FROM profile p
        JOIN user_point_transaction upt ON p.profile_id = upt.profile_id
        WHERE p.meeting_id = :meetingId AND p.church_id = :churchId and upt.sprint_id = :sprintId
        GROUP BY p.profile_id, p.first_name, p.last_name, p.meeting_id, p.church_id , upt.sprint_id
        limit :limit offset :offset
    """;

        List<Object[]> youth = entityManager.createNativeQuery(nativeSql)
                .setParameter("meetingId", meetingId)
                .setParameter("churchId", churchId)
                .setParameter("limit",limit)
                .setParameter("sprintId",sprintId)
                .setParameter("offset",offset).getResultList();

        List<YouthRankDto>  rankedYouth = new ArrayList<>();
        for (Object[] row : youth) {
            int rank = ((Number) row[0]).intValue();
            long profileId = ((Number) row[1]).intValue();
            String firstName = (String) row[2];
            String lastName = (String) row[3];
            double totalPoints = ((Number) row[4]).doubleValue();

            rankedYouth.add(new YouthRankDto(rank,firstName, lastName, totalPoints));
        }
        return rankedYouth;
    }

    @Override
    public double getYouthPoint(int profileId, int sprintId, int churchId, int meetingId) {
        String nativeSql =
                """
                    SELECT
                    SUM(upt.points) AS total_points
                    FROM profile p
                    JOIN user_point_transaction upt ON p.profile_id = upt.profile_id
                    WHERE p.meeting_id =:meetingId AND upt.church_id =:churchId  and upt.sprint_id =:sprintId and p.profile_id =:profileId
                    GROUP BY p.profile_id, p.first_name, p.last_name, p.meeting_id, p.church_id , upt.sprint_id
                """;
        try {

            System.out.println("meetingId churchId sprintId profileId " + meetingId + "," + churchId + "," + sprintId + "," + profileId);
            Object totalPoint = (Number) entityManager.createNativeQuery(nativeSql)
                    .setParameter("meetingId", meetingId)
                    .setParameter("churchId", churchId)
                    .setParameter("sprintId", sprintId)
                    .setParameter("profileId", profileId).getSingleResult();

            if (totalPoint != null) {
                if(totalPoint instanceof  Double){
                    return ((Double) totalPoint).doubleValue();
                }
            }
            return 00;
        }catch (NoResultException ex){
            return 00;
        }

    }

    @Override
    public int getSpecificYouthRank(int sprintId, int churchId, int meetingId , int profileId) {
        String  nativeQuery =
                """
                         select r from   (  SELECT
                                    DENSE_RANK() OVER (PARTITION BY p.meeting_id, p.church_id , upt.sprint_id ORDER BY SUM(upt.points) DESC) AS r,
                                    p.profile_id,
                                    p.first_name,
                                    p.last_name,
                                    SUM(upt.points) AS total_points
                                FROM profile p
                                JOIN user_point_transaction upt ON p.profile_id = upt.profile_id
                                WHERE p.meeting_id =:meetingId  AND p.church_id =:churchId and upt.sprint_id =:sprintId
                                GROUP BY p.profile_id, p.first_name, p.last_name, p.meeting_id, p.church_id , upt.sprint_id) ranks where ranks.profile_id =:profileId
                """;

        try {
            System.out.println("Parameters: meetingId=" + meetingId +
                               ", churchId=" + churchId +
                               ", sprintId=" + sprintId +
                               ", profileId=" + profileId);

            // Directly get the rank as Number and convert to int
            Number rank = (Number) entityManager.createNativeQuery(nativeQuery)
                    .setParameter("meetingId", meetingId)
                    .setParameter("churchId", churchId)
                    .setParameter("sprintId", sprintId)
                    .setParameter("profileId", profileId)
                    .getSingleResult();

            return rank != null ? rank.intValue() : 0;
        } catch (NoResultException ex) {
            return 0;  // Consistent return type (not 00 which is same as 0)
        }
    }

}
