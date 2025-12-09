package com.quoll.da.mapper;

import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Feedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FeedbackMapper {

    @Insert("INSERT INTO Feedback (teacherId, assetId, description, sendTime) " +
            "VALUES (#{teacherId}, #{assetId}, #{description}, #{sendTime})")
    @Options(useGeneratedKeys = true, keyProperty = "feedbackId")
    void raiseFeedback(Feedback feedback);


    @Update("UPDATE Feedback set status = 'Done' where feedbackId = #{feedbackId}")
    void makeAsDone(String feedbackId);

}
