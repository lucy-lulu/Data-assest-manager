package com.quoll.da.controller;

import com.quoll.da.pojo.Category;
import com.quoll.da.pojo.PageBean;
import com.quoll.da.pojo.Result;
import com.quoll.da.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/teacher")
    public Result getTeacherCategory(String teacherId) {
        List<Category> categoryList = categoryService.getTeacherCategory(teacherId);
        return Result.success(categoryList);
    }

    @PostMapping("/teacher/add")
    public Result addTeacherCategory(String teacherId, String categoryName) {
        if (categoryService.checkName(teacherId, categoryName) == 1) {
            return Result.error("This name already exists");
        }
        categoryService.addTeacherCategory(teacherId, categoryName);
        return Result.success();
    }

    @PutMapping("/teacher/rename")
    public Result renameTeacherCategory(String teacherId, String categoryId, String newName) {
        if (categoryService.checkName(teacherId, newName) == 1) {
            return Result.error("This name already exists");
        }
        categoryService.renameTeacherCategory(teacherId, categoryId, newName);
        return Result.success();
    }

    @DeleteMapping("/teacher/delete/{ids}")
    public Result deleteTeacherCategory(@PathVariable List<String> ids) {
        categoryService.clearTeacherCategory(ids);
        categoryService.deleteTeacherCategory(ids);
        return Result.success();
    }

    @PutMapping("/asset/classify")
    public Result classifyAsset(String categoryId, String assetId) { 
        if (categoryService.checkAsset(categoryId, assetId) == 1) {
            return Result.error("This asset already exists");
        }
        categoryService.classifyAsset(categoryId, assetId);
        return Result.success();
    }

    @DeleteMapping("/asset/remove/{ids}")
    public Result removeAsset(@PathVariable List<String> ids, String categoryId) {
        categoryService.removeAsset(ids, categoryId);
        return Result.success();
    }

    @GetMapping("/asset")
    public Result getCategoryAsset(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer pageSize, String categoryId) {
        PageBean pageBean = categoryService.getCategoryAsset(page, pageSize, categoryId);
        return Result.success(pageBean);
    }
}
