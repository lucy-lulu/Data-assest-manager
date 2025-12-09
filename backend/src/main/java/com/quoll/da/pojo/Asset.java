package com.quoll.da.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    private String assetId;
    private String assetName;
    private String format;
    private LocalDateTime createTime;
    private String author;
    private String description;
    private String tag;
    private String canvasLink;
    private String url;
  
    private String studentType;
    private String subjectNumber;
    private int visitCount;
  
    private MultipartFile file;
}
