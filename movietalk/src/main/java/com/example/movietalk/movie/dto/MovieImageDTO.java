package com.example.movietalk.movie.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import com.example.movietalk.movie.entity.MovieImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieImageDTO {
    private Long inum;
    private String uuid;
    private String path;
    private String imgName;
    private int ord;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public String getThumbnailURL() {
        String thumbFullPath = "";
        try {
            thumbFullPath = URLEncoder.encode(path + "/s_" + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return thumbFullPath;
    }

    public String getImageURL() {
        String fullPath = "";
        try {
            fullPath = URLEncoder.encode(path + "/" + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

    public static MovieImageDTO from(MovieImage movieImage) {
        return MovieImageDTO.builder()
                .inum(movieImage.getInum())
                .uuid(movieImage.getUuid())
                .path(movieImage.getPath())
                .imgName(movieImage.getImgName())
                .ord(movieImage.getOrd())
                .createDate(movieImage.getCreateDateTime())
                .updateDate(movieImage.getUpdateDateTime())
                .build();
    }
}
