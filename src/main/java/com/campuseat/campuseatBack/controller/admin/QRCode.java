package com.campuseat.campuseatBack.controller.admin;

import com.campuseat.campuseatBack.dto.admin.QRRequest;
import com.campuseat.campuseatBack.service.admin.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class QRCode {
    private final QrService qrService;

    @PostMapping("/createQR")
    public ResponseEntity<String> createQR(@RequestBody QRRequest request) {
        try{
            qrService.generatePdfAndSendEmail(request);
            return ResponseEntity.ok("PDF 전송 완료");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }
}
