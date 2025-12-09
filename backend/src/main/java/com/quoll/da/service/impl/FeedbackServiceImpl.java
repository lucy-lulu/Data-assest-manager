package com.quoll.da.service.impl;

import com.quoll.da.mapper.AssetMapper;
import com.quoll.da.mapper.FeedbackMapper;
import com.quoll.da.pojo.Feedback;
import com.quoll.da.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public void raiseFeedback(Feedback feedback){
        feedbackMapper.raiseFeedback(feedback);
    }

    @Override
    public void markAsDone(String feedbackId) {
        feedbackMapper.makeAsDone(feedbackId);
    }
}
