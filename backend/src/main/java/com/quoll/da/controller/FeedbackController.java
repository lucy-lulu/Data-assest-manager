package com.quoll.da.controller;


import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Feedback;
import com.quoll.da.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quoll.da.service.FeedbackService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RequestMapping("/feedback")
@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/addFeedback")
    public Result raiseFeedback(
            @RequestParam("teacherId") String teacherId,
            @RequestParam("assetId") String assetId,
            @RequestParam("description") String description) {

        // Create an Asset object
        Feedback feedback = new Feedback();
        feedback.setAssetId(assetId);
        feedback.setTeacherId(teacherId);
        feedback.setDescription(description);
        feedback.setSendTime(LocalDateTime.now());
        feedback.setStatus("In Progress");

        feedbackService.raiseFeedback(feedback);

        return Result.success();
    }

    @PutMapping("/markasdone")
    public Result markAsDone(String feedbackId) {
        // check
        feedbackService.markAsDone(feedbackId);
        return Result.success();
    }
}
