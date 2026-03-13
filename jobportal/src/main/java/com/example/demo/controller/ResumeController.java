package com.example.demo.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.util.FileDownloadUtil;

@Controller
public class ResumeController {

    @GetMapping("/resume/{userId}/{fileName}")
    public ResponseEntity<?> viewResume(
            @PathVariable String userId,
            @PathVariable String fileName,
            RedirectAttributes redirectAttributes) {

        FileDownloadUtil util = new FileDownloadUtil();
        Resource resource;

        try {
            resource = util.getFileAsResource("photos/candidate/" + userId, fileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error reading resume");
        }

      
        if (resource == null || !resource.exists()) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("No resume uploaded by this user.");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}