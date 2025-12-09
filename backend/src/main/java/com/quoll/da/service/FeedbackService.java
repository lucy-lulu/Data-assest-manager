package com.quoll.da.service;

import com.quoll.da.pojo.Feedback;

public interface FeedbackService {
    void raiseFeedback(Feedback feedback);

    void markAsDone(String feedbackId);
}
