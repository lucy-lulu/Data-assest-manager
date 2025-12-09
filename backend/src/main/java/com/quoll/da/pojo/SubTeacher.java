package com.quoll.da.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class SubTeacher {
    private String subTeacherId;
    private String username;
    private String passwordHash;
    private String email;
}
