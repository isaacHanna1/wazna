package com.watad.dao.reports;

import com.watad.dto.reports.DailyWaznaReport;

import java.time.LocalDate;
import java.util.List;

public interface WaznaReportDao {

    List<DailyWaznaReport> viewReportOfWaznaAddedToUsers(int sprintId,int churchId,int meetingId, LocalDate startFromDate , LocalDate endToDate , String profileId ,String point_source_type , String waznaType );
}
