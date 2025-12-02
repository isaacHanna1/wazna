package com.watad.dao.reports;

import com.watad.dto.reports.DailyWaznaReport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class WaznaReportDaoImp implements WaznaReportDao {

    private final EntityManager entityManager;

    public WaznaReportDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<DailyWaznaReport> viewReportOfWaznaAddedToUsers(int sprintId, int churchId,
                                                                int meetingId, LocalDate startFromDate,
                                                                LocalDate endToDate, String profileId,
                                                                String point_source_type, String waznaType , String bounce_type_filter , String service_class) {

        // Build dynamic SQL with named parameters
        StringBuilder nativeSql = new StringBuilder(
                "SELECT " +
                        "CONCAT(p.first_name, ' ', p.last_name) AS profile_name, " +
                        "p.service_class, " +
                        "ut.points, " +
                        "ut.transaction_type, " +
                        "ut.used_for, " +
                        "COALESCE(CONCAT(adder.first_name, ' ', adder.last_name), 'System') AS added_by_name, " +
                        "DATE_FORMAT(ut.transaction_date, '%d-%m-%Y') AS transaction_date " +
                        "FROM profile p " +
                        "JOIN user_point_transaction ut ON p.profile_id = ut.profile_id " +
                        "LEFT JOIN profile adder ON ut.added_by_profile_id = adder.profile_id " +
                        "left join user_bonus ub " +
                        "on ut.bonus_id = ub.bonus_id  "+
                        "WHERE ut.sprint_id = :sprintId " +
                        "AND ut.church_id = :churchId " +
                        "AND ut.meeting_id = :meetingId " +
                        "AND ut.transaction_date BETWEEN :startDate AND :endDate"
        );

        // Add conditions only if parameters are not "ALL"
        if (profileId != null && !profileId.equalsIgnoreCase("ALL") && !profileId.isEmpty()) {
            nativeSql.append(" AND p.profile_id = :profileId");
            System.out.println("  -> Adding profileId filter: " + profileId);
        }
        if (point_source_type != null && !point_source_type.equalsIgnoreCase("ALL") && !point_source_type.isEmpty()) {
            nativeSql.append(" AND ut.point_source_type = :point_source_type");
        }
        if (waznaType != null && !waznaType.equalsIgnoreCase("ALL") && !waznaType.isEmpty()) {
            nativeSql.append(" AND ut.transaction_type = :waznaType");
            System.out.println("  -> Adding waznaType filter: " + waznaType);
        }
        if (bounce_type_filter != null && !bounce_type_filter.equalsIgnoreCase("ALL") && !bounce_type_filter.isEmpty()) {
            nativeSql.append(" AND ub.bonus_type_id = :bounce_type_filter");
            System.out.println("  -> Adding bounce_type_filter filter: " + bounce_type_filter);
        }
        if(service_class !=null && !service_class.equalsIgnoreCase("ALL") & !service_class.isEmpty()){
            nativeSql.append(" AND p.service_class = :service_class");

        }
        // Debug: Print final SQL
        System.out.println("Final SQL: " + nativeSql.toString());

        try {
            Query query = entityManager.createNativeQuery(nativeSql.toString());

            // Set required parameters
            query.setParameter("sprintId", sprintId);
            query.setParameter("churchId", churchId);
            query.setParameter("meetingId", meetingId);
            query.setParameter("startDate", startFromDate.atStartOfDay());
            query.setParameter("endDate", endToDate.atTime(23, 59, 59));

            // Debug: Print parameter values
            System.out.println("Parameter Values:");
            System.out.println("  sprintId: " + sprintId);
            System.out.println("  churchId: " + churchId);
            System.out.println("  meetingId: " + meetingId);
            System.out.println("  startDate: " + startFromDate.atStartOfDay());
            System.out.println("  endDate: " + endToDate.atTime(23, 59, 59));

            // Set optional parameters only if needed
            if (profileId != null && !profileId.equalsIgnoreCase("ALL") && !profileId.isEmpty()) {
                int profileIdInt = Integer.parseInt(profileId);
                query.setParameter("profileId", profileIdInt);
                System.out.println("  profileId: " + profileIdInt);
            }
            if (point_source_type != null && !point_source_type.equalsIgnoreCase("ALL") && !point_source_type.isEmpty()) {
                query.setParameter("point_source_type", point_source_type);
                System.out.println("  point_source_type: " + point_source_type);
            }
            if (waznaType != null && !waznaType.equalsIgnoreCase("ALL") && !waznaType.isEmpty()) {
                query.setParameter("waznaType", waznaType);
                System.out.println("  waznaType: " + waznaType);
            }
            if (bounce_type_filter != null && !bounce_type_filter.equalsIgnoreCase("ALL") && !bounce_type_filter.isEmpty()) {
                query.setParameter("bounce_type_filter", bounce_type_filter);
                System.out.println("  bounce_type_filter : " + bounce_type_filter);
            }
            if(service_class !=null && !service_class.equalsIgnoreCase("ALL") & !service_class.isEmpty()) {
                query.setParameter("service_class", service_class);
            }

                @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();

            // Debug: Print number of results
            System.out.println("Number of results from database: " + results.size());

            // Convert Object[] to DailyWaznaReport
            List<DailyWaznaReport> reports = new ArrayList<>();

            for (Object[] result : results) {
                // Debug: Print each result row
                System.out.println("Result Row: " + Arrays.toString(result));

                DailyWaznaReport report = new DailyWaznaReport(
                        (String) result[0], // profile_name
                        (String) result[1], // service_class
                        String.valueOf(result[2]), // points as String
                        (String) result[3], // transaction_type
                        (String) result[4], // used_for
                        (String) result[5], // added_by_name
                        (String) result[6]  // transaction_date in dd-mm-yyyy format
                );
                reports.add(report);
            }

            System.out.println("=== DAO DEBUG END - Returning " + reports.size() + " records ===");
            return reports;

        } catch (Exception e) {
            System.out.println("=== DAO ERROR ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching daily reports: " + e.getMessage(), e);
        }
    }
}