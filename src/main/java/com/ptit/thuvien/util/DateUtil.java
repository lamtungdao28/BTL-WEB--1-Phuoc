package com.ptit.thuvien.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp tiện ích chung
 */
public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Format ngày theo kiểu Việt Nam: dd/MM/yyyy
     */
    public static String formatNgay(LocalDate date) {
        return date != null ? date.format(DATE_FORMAT) : "";
    }

    /**
     * Format ngày giờ theo kiểu Việt Nam: dd/MM/yyyy HH:mm
     */
    public static String formatNgayGio(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMAT) : "";
    }

    /**
     * Kiểm tra phiếu mượn có quá hạn không
     */
    public static boolean laQuaHan(LocalDate ngayHenTra) {
        return ngayHenTra != null && LocalDate.now().isAfter(ngayHenTra);
    }
}
