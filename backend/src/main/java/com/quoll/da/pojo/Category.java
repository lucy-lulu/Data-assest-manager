package com.quoll.da.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String categoryId;
    private String categoryName;
    private String teacherId;
    private Integer total;
}
