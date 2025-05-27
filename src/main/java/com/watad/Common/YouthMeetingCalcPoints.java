package com.watad.Common;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

public class YouthMeetingCalcPoints {



    /**
     * Calculates the number of points a user should earn based on when they scanned a QR code.
     * The attendance window is divided into four equal time periods (quarters).
     * Points are assigned based on how early the user scanned within the valid time range.
     *
     * Used in: QR-based attendance system for youth meetings.
     *
     * @param fromTime    The start time of the valid QR scan window (e.g., 22:00).
     * @param toTime      The end time of the valid QR scan window (e.g., 23:00).
     * @param totalPoints The full number of points awarded for scanning in the first quarter.
     * @param qrTime      The actual time the QR was scanned.
     * @return The number of points earned based on the scan time.
     */
    public static int calculatePoints(LocalTime fromTime, LocalTime toTime, int totalPoints, LocalTime qrTime) {
        long totalMinutes = Duration.between(fromTime, toTime).toMinutes();
        long quarterMinutes = totalMinutes / 4;

        LocalTime q1End = fromTime.plusMinutes(quarterMinutes);
        LocalTime q2End = q1End.plusMinutes(quarterMinutes);
        LocalTime q3End = q2End.plusMinutes(quarterMinutes);
        LocalTime q4End = toTime; // last quarter

        if (!qrTime.isBefore(fromTime) && !qrTime.isAfter(toTime)) {
            if (!qrTime.isAfter(q1End)) {
                return totalPoints;
            } else if (!qrTime.isAfter(q2End)) {
                return totalPoints / 2;
            } else if (!qrTime.isAfter(q3End)) {
                return totalPoints / 4;
            } else {
                return 0;
            }
        } else {
            return 0; // outside valid time range
        }
    }



}
