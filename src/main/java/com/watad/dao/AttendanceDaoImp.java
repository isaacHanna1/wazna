package com.watad.dao;

import com.watad.dto.attendance.AttendanceStatusResponse;
import com.watad.entity.Attendance;
import com.watad.entity.Profile;
import com.watad.entity.QrCode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttendanceDaoImp implements AttendanceDao{

    private final EntityManager entityManager;

    public AttendanceDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Attendance attendance) {
        entityManager.persist(attendance);
    }

    @Override
    public List<AttendanceStatusResponse> getAttendanceResponse(int churchId, int meetingId, int qrCodeId, String serviceClass , String role) {
        // build query dynamically — add service_class only when provided
        boolean filterByClass = serviceClass != null && !serviceClass.trim().isEmpty();
        boolean filterByRole = role != null && !role.trim().isEmpty();

        // we need update make the user only active and check thr role of user

        String hql = """
        SELECT p, a , q FROM Profile p
        LEFT JOIN User u       ON u.profile.id = p.id
        LEFT JOIN Attendance a ON a.attendanceId.user.id = u.id AND a.attendanceId.qrCode.id = :qrCodeId
        LEFT JOIN u.roles r
        LEFT     JOIN QrCode q ON q.id = :qrCodeId
        WHERE p.meetings.id = :meetingId
          AND p.church.id  = :churchId
          AND u.isEnabled = true
        """
                + (filterByClass ? " AND p.serviceClass = :serviceClass " : "")
                + (filterByRole  ? "AND r.roleName = :roleName " :"")
                + " ORDER BY p.serviceClass , p.firstName, p.lastName ";

        var query = entityManager.createQuery(hql, Object[].class)
                .setParameter("qrCodeId",  qrCodeId)
                .setParameter("meetingId", meetingId)
                .setParameter("churchId",  churchId);

        if (filterByClass) {
            query.setParameter("serviceClass", serviceClass);
        }
        if (filterByRole){
            query.setParameter("roleName",role);
        }
        List<Object[]> rows = query.getResultList();

        return rows.stream().map(row -> {
            Profile p = (Profile)    row[0];
            Attendance a = (Attendance) row[1];   // null if absent
            QrCode q = (QrCode) row[2];

            AttendanceStatusResponse dto = new AttendanceStatusResponse();
            dto.setProfileId(p.getId());
            dto.setFirst_name(p.getFirstName());
            dto.setLast_name(p.getLastName());
            dto.setPhone(p.getPhone());
            dto.setGendre(p.getGender());
            dto.setEmail(p.getEmail());
            dto.setService_class(p.getServiceClass());
            dto.setAttendance_status(a != null ? "Present" : "Absent");
            dto.setScanned_at(a != null && a.getScannedAt() != null
                    ? a.getScannedAt().toString() : null);
            if (q != null && q.getValidDate() != null) {
                dto.setValid_date(q.getValidDate().toString());
            }
            return dto;
        }).toList();
    }


}
