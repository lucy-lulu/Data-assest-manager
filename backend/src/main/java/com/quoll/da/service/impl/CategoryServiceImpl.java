package com.quoll.da.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.quoll.da.mapper.CategoryMapper;
import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Category;
import com.quoll.da.pojo.PageBean;
import com.quoll.da.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper; 

    @Override
    public List<Category> getTeacherCategory(String teacherId) {
        List<Category> categoryList = categoryMapper.getTeacherCategory(teacherId);
        System.out.println("get teacher category: " + categoryList);
        return categoryList;
    }

    @Override
    public Integer checkName(String teacherId, String categoryName) {
        return categoryMapper.checkName(teacherId, categoryName);
    }

    @Override
    public void addTeacherCategory(String teacherId, String categoryName) {
        System.out.println("add teacher category: teacherId = " + teacherId + " and categoryName = " + categoryName);
        categoryMapper.addTeacherCategory(teacherId, categoryName);
    }

    @Override
    public void renameTeacherCategory(String teacherId, String categoryId, String newName) {
        System.out.println("rename teacher category: teacherId = " + teacherId + " and categoryId = " + categoryId + " and newName = " + newName);
        categoryMapper.renameTeacherCategory(teacherId, categoryId, newName);
    }

    @Override
    public void clearTeacherCategory(List<String> ids) {
        System.out.println("clear teacher categories: ids = " + ids);
        categoryMapper.clearTeacherCategory(ids);
    }

    @Override
    public void deleteTeacherCategory(List<String> ids) {
        System.out.println("delete teacher categories: ids = " + ids);
        categoryMapper.deleteTeacherCategory(ids);
    }

    @Override
    public Integer checkAsset(String categoryId, String assetId) {
        return categoryMapper.checkAsset(categoryId, assetId);
    }

    @Override
    public void classifyAsset(String categoryId, String assetId) {
        System.out.println("classify asset into category: categoryId = " + categoryId + " and assetId = " + assetId);
        categoryMapper.classifyAsset(categoryId, assetId);
    }

    @Override
    public void removeAsset(List<String> ids, String categoryId) {
        System.out.println("remove assets in category: ids = " + ids + " and categoryId = " + categoryId);
        categoryMapper.removeAsset(ids, categoryId);
    }

    @Override
    public PageBean getCategoryAsset(Integer page, Integer pageSize, String categoryId) {
        PageHelper.startPage(page, pageSize);
        List<Asset> assetList = categoryMapper.getCategoryAsset(categoryId);
        Page<Asset> p = (Page<Asset>) assetList;
        return new PageBean(p.getTotal(), p.getResult());
    }
}
