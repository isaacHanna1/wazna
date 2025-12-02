package com.watad.services.reports;

import com.watad.dao.reports.WaznaReportDao;
import com.watad.dto.reports.DailyWaznaReport;

import java.time.LocalDate;
import java.util.List;

public interface WaznaReportServices {

    public List<DailyWaznaReport> viewReportOfWaznaAddedToUsers(int sprintId, LocalDate startFromDate,
                                                                LocalDate endToDate, String profileId,
                                                                String point_source_type, String waznaType , String bounce_type_filter, String service_class);
}
