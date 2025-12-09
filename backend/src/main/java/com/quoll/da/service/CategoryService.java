package com.quoll.da.service;

import com.quoll.da.pojo.Category;
import com.quoll.da.pojo.PageBean;

import java.util.List;

public interface CategoryService {
    //get categories of the current teacher
    List<Category> getTeacherCategory(String teacherId);

    //check whether this category name already exists for the current teacher
    Integer checkName(String teacherId, String categoryName);

    //add a category of the current teacher
    void addTeacherCategory(String teacherId, String categoryName);

    //rename a category of the current teacher
    void renameTeacherCategory(String teacherId, String categoryId, String newName);

    //clear assets of categories by their ids
    void clearTeacherCategory(List<String> ids);

    //delete categories of the current teacher by their ids
    void deleteTeacherCategory(List<String> ids);

    //check whether this asset already exists in this category
    Integer checkAsset(String categoryId, String assetId);

    //classify an asset into a category
    void classifyAsset(String categoryId, String assetId);

    //remove assets in a category by their ids
    void removeAsset(List<String> ids, String categoryId);

    PageBean getCategoryAsset(Integer page, Integer pageSize, String categoryId);
}
