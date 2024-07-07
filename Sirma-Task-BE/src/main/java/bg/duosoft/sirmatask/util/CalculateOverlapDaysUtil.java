package bg.duosoft.sirmatask.util;

import bg.duosoft.sirmatask.dtos.EmployeeDTO;

import java.util.Date;

public class CalculateOverlapDaysUtil {

    public static long calculateOverlapDays(EmployeeDTO e1, EmployeeDTO e2) {
        Date start = e1.getDateFrom().after(e2.getDateFrom()) ? e1.getDateFrom() : e2.getDateFrom();
        Date end1 = e1.getDateTo() == null ? new Date() : e1.getDateTo();
        Date end2 = e2.getDateTo() == null ? new Date() : e2.getDateTo();
        Date end = end1.before(end2) ? end1 : end2;

        if (start.before(end)) {
            long diff = end.getTime() - start.getTime();
            return diff / (1000 * 60 * 60 * 24);
        } else {
            return 0;
        }
    }
}
