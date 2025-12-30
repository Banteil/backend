package com.example.movietalk.common.controller;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.movietalk.movie.dto.MovieImageDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequestMapping("/upload")
public class UploadController {
    @Value("${com.example.movietalk.upload.path}")
    private String uploadPath;

    @GetMapping("/upload")
    public void getUpload() {
        log.info("업로드 폼 요청");
    }

    @ResponseBody
    @PostMapping("/upload")
    public List<MovieImageDTO> postUpload(@RequestParam("uploadFiles") MultipartFile[] uploadFiles) {
        // 1. 날짜 경로 생성 (2025/12/30)
        String datePath = makeDir();
        // 2. DB에 저장될 상대 경로 (temp/2025/12/30)
        String dbSavePath = "temp" + File.separator + datePath;

        // 3. uploadPath를 절대 경로로 변환
        String absolutePath = new File(uploadPath).getAbsolutePath();

        // 4. 실제 저장될 폴더 (절대경로/temp/날짜)
        File saveDir = new File(absolutePath + File.separator + dbSavePath);

        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        List<MovieImageDTO> upList = new ArrayList<>();

        for (MultipartFile file : uploadFiles) {
            String oriName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();

            // 5. 최종 파일 객체 (절대경로/temp/날짜/uuid_name.jpg)
            File saveFile = new File(saveDir.getAbsolutePath() + File.separator + uuid + "_" + oriName);

            try {
                // 절대 경로가 문자열로 완벽하게 조립되었으므로 에러 없이 저장될 것입니다.
                file.transferTo(saveFile);

                File thumbSaveFile = new File(saveDir.getAbsolutePath() + File.separator + "s_" + uuid + "_" + oriName);
                Thumbnailator.createThumbnail(saveFile, thumbSaveFile, 100, 100);

                upList.add(MovieImageDTO.builder()
                        .imgName(oriName)
                        .uuid(uuid)
                        .path(dbSavePath)
                        .build());
            } catch (Exception e) {
                log.error("Upload Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return upList;
    }

    private String makeDir() {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        File file = new File(uploadPath, dateStr);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dateStr;
    }

    @ResponseBody
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(@RequestParam("fileName") String fileName) {
        ResponseEntity<byte[]> result = null;
        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Request fileName: " + srcFileName);

            File file = new File(new File(uploadPath).getAbsolutePath() + File.separator + srcFileName);
            log.info("Full File Path: " + file.getAbsolutePath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Display Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFile(@RequestParam("fileName") String fileName) {
        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(new File(uploadPath).getAbsolutePath() + File.separator + srcFileName);

            // 썸네일 파일도 같이 삭제
            File thumbFile = new File(file.getParent(), "s_" + file.getName());

            if (thumbFile.exists())
                thumbFile.delete();
            if (file.exists())
                file.delete();

            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
