package com.watad.services.reports;

import com.watad.dao.reports.WaznaReportDao;
import com.watad.dto.reports.DailyWaznaReport;
import com.watad.services.UserServices;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WaznaReportServiceImp implements WaznaReportServices {

    private final WaznaReportDao waznaReportDao;
    private final UserServices userServices;

    public WaznaReportServiceImp(WaznaReportDao waznaReportDao, UserServices userServices) {
        this.waznaReportDao = waznaReportDao;
        this.userServices = userServices;
    }

    @Override
    public List<DailyWaznaReport> viewReportOfWaznaAddedToUsers(int sprintId,
                                                                LocalDate startFromDate,
                                                                LocalDate endToDate, String profileId,
                                                                String point_source_type, String waznaType) {
        int churchId  = userServices.getLogInUserChurch().getId();
        int meetingId = userServices.getLogInUserMeeting().getId();
        return waznaReportDao.viewReportOfWaznaAddedToUsers(
                sprintId, churchId, meetingId,
                startFromDate, endToDate,
                profileId, point_source_type, waznaType
        );
    }
}