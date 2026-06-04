package com.ptit.thuvien.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Xử lý lỗi toàn cục cho ứng dụng
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý lỗi 404 - Không tìm thấy trang
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(Model model) {
        model.addAttribute("loiTitle", "404 - Không tìm thấy");
        model.addAttribute("loiMessage", "Trang bạn tìm kiếm không tồn tại.");
        return "error/404";
    }

    /**
     * Xử lý lỗi chung - Runtime Exception
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("loiTitle", "Lỗi hệ thống");
        model.addAttribute("loiMessage", ex.getMessage());
        return "error/500";
    }

    /**
     * Xử lý lỗi chung - Exception
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("loiTitle", "Đã xảy ra lỗi");
        model.addAttribute("loiMessage", "Vui lòng thử lại sau hoặc liên hệ quản trị viên.");
        return "error/500";
    }
}
