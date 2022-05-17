package com.xiamu.item.mapper;

import com.xiamu.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface CategoryMapper extends Mapper<Category>,SelectByIdListMapper<Category,Long>{

}
