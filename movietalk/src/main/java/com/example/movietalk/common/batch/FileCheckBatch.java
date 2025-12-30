package com.example.movietalk.common.batch;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.repository.MovieImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class FileCheckBatch {

    private final MovieImageRepository imageRepository; // MovieImage 테이블 접근용

    @Value("${com.example.movietalk.upload.path}")
    private String uploadPath;

    // 5분마다 실행 (밀리초 단위: 1000 * 60 * 5)
    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void checkFiles() {
        log.info("---------------------------------------");
        log.info("File Check Batch Running........");

        // 1. DB에 등록된 어제~오늘의 모든 파일 목록 가져오기 (비교 대상)
        // 실제로는 전체를 다 가져오기보다 특정 날짜 범위를 지정하는 것이 좋으나 테스트용으로 전체 조회
        List<MovieImage> imageList = imageRepository.findAll();

        // DB 파일 경로 리스트 (uuid_imgName 형태)
        List<Path> fileListPaths = imageList.stream()
                .map(img -> Paths.get(uploadPath, img.getPath(), img.getUuid() + "_" + img.getImgName()))
                .collect(Collectors.toList());

        // 썸네일 경로도 포함
        fileListPaths.addAll(imageList.stream()
                .map(img -> Paths.get(uploadPath, img.getPath(), "s_" + img.getUuid() + "_" + img.getImgName()))
                .collect(Collectors.toList()));

        // 2. 실제 upload 폴더 내의 파일들 확인
        File rootDir = new File(uploadPath);
        if (!rootDir.exists())
            return;

        // 실제 물리 파일들을 순회하며 DB에 없는 것들 추출
        cleanInvalidFiles(rootDir, fileListPaths);
    }

    private void cleanInvalidFiles(File targetDir, List<Path> dbFileList) {
        File[] files = targetDir.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                // 재귀적으로 하위 폴더(날짜 폴더) 탐색
                cleanInvalidFiles(file, dbFileList);

                // 빈 폴더가 되면 삭제 (선택 사항)
                if (file.listFiles().length == 0 && file.getName().equals("temp") == false) {
                    file.delete();
                }
            } else {
                // 파일인 경우
                Path currentPath = file.toPath();

                // DB 목록에 없고, temp 파일이 아니라면 삭제 (실제 저장공간 청소)
                if (!dbFileList.contains(currentPath)) {
                    // 여기서 temp 폴더 내의 파일 중 생성된지 5분이 넘은 것도 함께 삭제 로직 추가 가능
                    if (isOldTempFile(file)) {
                        log.warn("정리 대상 파일 삭제: " + file.getAbsolutePath());
                        file.delete();
                    }
                }
            }
        }
    }

    // 임시 파일이거나 DB에 없는 파일 중 삭제할 조건 (5분 경과)
    private boolean isOldTempFile(File file) {
        long diff = System.currentTimeMillis() - file.lastModified();
        long fiveMinutes = 5 * 60 * 1000;

        // temp 폴더에 들어있거나, DB에 없는데 생성된지 5분이 지났다면 삭제 대상으로 판정
        return diff > fiveMinutes;
    }
}