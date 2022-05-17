package com.xiamu.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiamu.common.pojo.PageResult;
import com.xiamu.item.mapper.BrandMapper;
import com.xiamu.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired(required = false)
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        Example example=new Example(Brand.class);
        Example.Criteria criteria=example.createCriteria();
        //根据name模糊查询，或者根据首字母查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        //添加分页条件
        PageHelper.startPage(page,rows);
        //添加排序条件
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc ? "desc" :"asc") );
        }
        List<Brand> brands = this.brandMapper.selectByExample(example);
        //包装成pageinfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        //包装成分页结果返回
        System.out.println("example的内容是"+example);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }
    @Transactional
    public void addBrands(Brand brand,List<Long> cids) {
        this.brandMapper.insertSelective(brand);
        cids.forEach(cid->{
            this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
        });
    }

    public List<Brand> queryBrandByCid(Long cid) {

        return this.brandMapper.selectBrandByCid(cid);
    }

    public Brand queryBrandById(Long id) {
        return  this.brandMapper.selectByPrimaryKey(id);
    }
}
