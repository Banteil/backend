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
    @PostMapping("upload")
    public List<MovieImageDTO> postUpload(MultipartFile[] uploadFiles) {
        String saveDirPath = makeDir();
        List<MovieImageDTO> upList = new ArrayList<>();

        for (MultipartFile file : uploadFiles) {
            log.info("OriginalFilename {}", file.getOriginalFilename());
            log.info("size {}", file.getSize());
            log.info("content type {}", file.getContentType());

            String oriName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String absolutePath = new File(uploadPath).getAbsolutePath();

            File saveFile = new File(absolutePath + File.separator + saveDirPath, uuid + "_" + oriName);
            // String saveName = uploadPath + File.separator + saveDirPath + File.separator
            // + uuid + "_" + oriName;

            upList.add(MovieImageDTO.builder().imgName(oriName).uuid(uuid).path(saveDirPath).build());

            try {
                // 저장
                file.transferTo(saveFile);
            } catch (Exception e) {
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
    public ResponseEntity<byte[]> getFile(String fileName) {
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

}
