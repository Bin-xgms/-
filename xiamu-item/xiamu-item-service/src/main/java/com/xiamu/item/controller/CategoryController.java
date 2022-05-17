package com.xiamu.item.controller;

import com.xiamu.item.pojo.Category;
import com.xiamu.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesById(@RequestParam(value = "pid",defaultValue ="0" )Long pid){
        if(pid == null || pid<0){
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = this.categoryService.queryCategoriesById(pid);
        if(categories==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam List<Long> ids){
        if(ids == null || CollectionUtils.isEmpty(ids)){
            return ResponseEntity.badRequest().build();
        }
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if(CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);

    }



}
