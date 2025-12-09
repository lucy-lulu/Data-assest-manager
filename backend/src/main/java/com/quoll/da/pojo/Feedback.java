package com.quoll.da.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    private String feedbackId;

    private String teacherId;

    private String assetId;

    private String description;

    private LocalDateTime sendTime;

    private String status;


}
