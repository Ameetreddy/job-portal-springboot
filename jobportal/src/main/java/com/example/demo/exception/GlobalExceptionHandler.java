package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /* -------------------------
       HANDLE ALL RUNTIME ERRORS
    ------------------------- */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {

        log.error("Runtime exception occurred", ex);

        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error"; // maps to error.html
    }

    /* -------------------------
       FALLBACK FOR ANY EXCEPTION
    ------------------------- */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {

        log.error("Unexpected exception occurred", ex);

        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute(
                "errorMessage",
                "An unexpected error occurred. Please try again later.");

        return "error";
    }
}
