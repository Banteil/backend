package com.example.movietalk.movie.service;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.common.dto.PageRequestDTO;
import com.example.movietalk.common.dto.PageResultDTO;
import com.example.movietalk.movie.dto.MovieDTO;
import com.example.movietalk.movie.dto.MovieImageDTO;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.entity.Review;
import com.example.movietalk.movie.repository.MovieImageRepository;
import com.example.movietalk.movie.repository.MovieRepository;
import com.example.movietalk.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
@Setter
@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;
    private final ReviewRepository reviewRepository;

    @Value("${com.example.movietalk.upload.path}")
    private String uploadPath;

    @Transactional
    public Long register(MovieDTO dto) {
        // 1. 파일 먼저 옮기고 DTO의 path를 실제 경로로 갱신
        if (dto.getMImages() != null) {
            dto.getMImages().forEach(imgDTO -> {
                if (imgDTO.getPath().startsWith("temp"))
                    moveTempToReal(imgDTO);
            });
        }

        // 2. 경로가 모두 수정된 DTO를 사용하여 엔티티 생성
        Movie movie = Movie.from(dto);
        movieRepository.save(movie);

        return movie.getMno();
    }

    @Transactional
    public Long update(MovieDTO dto) {
        log.info("Movie update Start. DTO: {}", dto);
        // 1. 기존 영화 및 이미지 정보 로드
        Movie movie = movieRepository.findById(dto.getMno())
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 없습니다. mno=" + dto.getMno()));
        // 2. 제목 변경
        movie.setTitle(dto.getTitle());
        // 3. 기존 이미지 연관관계 제거 (orphanRemoval=true 설정 시 DB 자동 삭제)
        movie.getImages().clear();

        // 4. 새로운 이미지 리스트 처리
        if (dto.getMImages() != null && !dto.getMImages().isEmpty()) {
            for (int i = 0; i < dto.getMImages().size(); i++) {
                MovieImageDTO imgDTO = dto.getMImages().get(i);

                // [파일 이동] 경로에 temp가 포함된 경우 실제 저장소로 이동
                if (imgDTO.getPath().contains("temp")) {
                    moveTempToReal(imgDTO);
                }

                // [엔티티 생성] DTO에서 순서(ord)가 관리되지 않는다면 인덱스 i를 사용 가능
                MovieImage movieImage = MovieImage.builder()
                        .uuid(imgDTO.getUuid())
                        .path(imgDTO.getPath()) // moveTempToReal 거친 후에는 temp가 제거된 경로임
                        .imgName(imgDTO.getImgName())
                        .ord(i) // 화면에서의 순서 유지
                        .movie(movie)
                        .build();

                movie.addImage(movieImage);
            }
        }

        return movie.getMno();
    }

    @Transactional(readOnly = true)
    public PageResultDTO<MovieDTO> getMovieList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("mno").descending());

        Page<MovieDTO> result = movieRepository.getListPageQuerydsl(pageable);

        return PageResultDTO.<MovieDTO>withAll()
                .dtoList(result.getContent())
                .totalCount(result.getTotalElements())
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public MovieDTO getMovie(Long mno) {
        Movie movie = movieRepository.findById(mno).orElseThrow();
        List<Review> reviews = reviewRepository.findByMovie(movie);
        MovieDTO movieDTO = MovieDTO.from(movie);

        // 리뷰 통계 계산 및 세팅
        movieDTO.setReviewCnt((long) reviews.size());
        double avg = reviews.stream()
                .mapToInt(Review::getGrade)
                .average()
                .orElse(0.0);
        movieDTO.setAvg(avg);

        return movieDTO;
    }

    @Transactional
    public void removeWithFiles(Long mno) {
        // 1. 삭제 전 이미지 정보 조회 (파일을 지우기 위해 경로 정보 필요)
        Movie movie = movieRepository.findById(mno)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다. mno=" + mno));

        List<MovieImage> imageList = movie.getImages();

        // 2. 물리 파일 삭제 로직 실행
        if (imageList != null && !imageList.isEmpty()) {
            imageList.forEach(img -> {
                deletePhysicalFile(img.getPath(), img.getUuid(), img.getImgName());
            });
        }

        // 3. DB 데이터 삭제 (cascade 옵션에 의해 이미지 테이블도 함께 삭제됨)
        movieRepository.deleteById(mno);
    }

    /**
     * 물리적 파일 삭제 (원본 + 썸네일)
     */
    private void deletePhysicalFile(String path, String uuid, String name) {
        try {
            String absolutePath = new File(uploadPath).getAbsolutePath();
            String fileName = uuid + "_" + name;

            // 원본 파일 객체
            File file = new File(absolutePath + File.separator + path + File.separator + fileName);
            // 썸네일 파일 객체
            File thumbFile = new File(absolutePath + File.separator + path + File.separator + "s_" + fileName);

            if (file.exists())
                file.delete();
            if (thumbFile.exists())
                thumbFile.delete();

            log.info("물리 파일 삭제 성공: " + fileName);
        } catch (Exception e) {
            log.error("파일 삭제 중 오류: " + e.getMessage());
        }
    }

    private void moveTempToReal(MovieImageDTO imgDTO) {
        try {
            // 원본 경로: temp/2025/12/30 -> 실제 경로: 2025/12/30
            String tempPath = imgDTO.getPath();
            String realPath = tempPath.replace("temp" + File.separator, "")
                    .replace("temp/", ""); // OS 호환성 고려

            File rootDir = new File(uploadPath);
            File sourceDir = new File(rootDir, tempPath);
            File targetDir = new File(rootDir, realPath);

            if (!targetDir.exists())
                targetDir.mkdirs();

            String fileName = imgDTO.getUuid() + "_" + imgDTO.getImgName();

            // 1. 원본 파일 이동 (renameTo)
            File srcFile = new File(sourceDir, fileName);
            File destFile = new File(targetDir, fileName);
            if (srcFile.exists())
                srcFile.renameTo(destFile);

            // 2. 썸네일 파일 이동 (s_ 추가)
            File srcThumb = new File(sourceDir, "s_" + fileName);
            File destThumb = new File(targetDir, "s_" + fileName);
            if (srcThumb.exists())
                srcThumb.renameTo(destThumb);

            // 3. DTO의 경로를 DB에 저장될 실제 경로로 업데이트
            imgDTO.setPath(realPath);

            log.info("Successfully moved file to: " + destFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("File Move Error: " + e.getMessage());
        }
    }
}
