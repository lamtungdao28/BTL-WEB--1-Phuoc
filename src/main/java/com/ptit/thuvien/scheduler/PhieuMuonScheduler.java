package com.ptit.thuvien.scheduler;

import com.ptit.thuvien.service.PhieuMuonService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Lập lịch tự động chạy ngầm để quét và cập nhật trạng thái phiếu mượn quá hạn
 */
@Component
@RequiredArgsConstructor
public class PhieuMuonScheduler {

    private final PhieuMuonService phieuMuonService;

    /**
     * Tự động quét và cập nhật các phiếu mượn quá hạn mỗi 1 phút (60000ms)
     */
    @Scheduled(fixedRate = 60000)
    public void autoCheckOverdueSlips() {
        System.out.println("[Scheduler] Bắt đầu quét tự động phiếu mượn quá hạn...");
        try {
            phieuMuonService.capNhatTrangThaiQuaHan();
            System.out.println("[Scheduler] Hoàn tất quét tự động phiếu mượn quá hạn.");
        } catch (Exception e) {
            System.err.println("[Scheduler] Lỗi khi quét tự động phiếu mượn quá hạn: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
