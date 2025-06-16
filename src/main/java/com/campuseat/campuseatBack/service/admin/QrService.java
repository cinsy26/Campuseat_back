package com.campuseat.campuseatBack.service.admin;

import com.campuseat.campuseatBack.dto.admin.QRRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import com.itextpdf.text.Document;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;


@Service
@RequiredArgsConstructor
public class QrService {

    private final JavaMailSender mailSender;

    public void generatePdfAndSendEmail(QRRequest request) throws Exception {
        List<BufferedImage> qrImages = new ArrayList<>();

        for(int i = 1; i <= request.getSeatCount(); i++) {
            String content = String.format(
                    "http://52.79.181.144:8080/seat/check?building=%s&location=%s&seat=%s",
                    URLEncoder.encode(request.getBuildingName(), "UTF-8"),
                    URLEncoder.encode(request.getLocationName(), "UTF-8"),
                    URLEncoder.encode(i + "번", "UTF-8") // <--- 이 부분!
            );
            qrImages.add(generateQrImage(content));
        }

        File pdfFile = createPdfWithQrCodes(qrImages, request);
        sendEmailWithAttachment(request.getEmail(), pdfFile);
    }

    private BufferedImage generateQrImage(String text) throws WriterException {
        int size = 200;
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    private File createPdfWithQrCodes(List<BufferedImage> qrImages, QRRequest request) throws Exception {
        File tempFile = File.createTempFile("qr_output", ".pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(tempFile));
        document.open();

        PdfPTable table = new PdfPTable(4); // 4 columns
        table.setWidthPercentage(100);

        //한글 폰트 설정하기
        String fontPath = Paths.get(
                getClass().getClassLoader().getResource("fonts/NanumGothic.ttf").toURI()
        ).toString();        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font koreanFont = new Font(baseFont, 10);

        for (int i = 0; i < qrImages.size(); i++) {
            BufferedImage image = qrImages.get(i);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            Image pdfImage = Image.getInstance(baos.toByteArray());
            pdfImage.scaleToFit(100, 100);

            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); // 셀 전체 가운데 정렬
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);   // 수직 가운데 (선택)

// 이미지
            pdfImage.setAlignment(Image.ALIGN_CENTER); //이미지 수평 가운데
            cell.addElement(pdfImage);

// 텍스트
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER); //문단도 수평 가운데
            paragraph.add(new Chunk(request.getBuildingName() + "\n", koreanFont));
            paragraph.add(new Chunk(request.getLocationName() + "\n", koreanFont));
            paragraph.add(new Chunk("좌석번호: " + (i + 1), koreanFont));
            cell.addElement(paragraph);


            table.addCell(cell);
        }

        int remainder = qrImages.size() % 4;
        if (remainder != 0) {
            int emptyCells = 4 - remainder;
            for (int j = 0; j < emptyCells; j++) {
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorder(Rectangle.NO_BORDER);
                emptyCell.setPadding(5);
                table.addCell(emptyCell);
            }
        }

        //테이블 추가 및 닫기!
        document.add(table);
        document.close();

        return tempFile;
    }


    private void sendEmailWithAttachment(String to, File pdfFile) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("QR 코드 PDF 파일");
        helper.setText("요청하신 QR 코드 PDF 파일을 첨부합니다.");
        helper.addAttachment("qr_codes.pdf", pdfFile);

        mailSender.send(message);
    }

}
