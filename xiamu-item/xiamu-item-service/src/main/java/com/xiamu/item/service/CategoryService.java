package com.xiamu.item.service;

import com.xiamu.item.mapper.CategoryMapper;
import com.xiamu.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoriesById(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return this.categoryMapper.select(category);
    }
    public List<String> queryNamesByIds(List<Long> cids){
        List<Category> categories = this.categoryMapper.selectByIdList(cids);
        return categories.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
