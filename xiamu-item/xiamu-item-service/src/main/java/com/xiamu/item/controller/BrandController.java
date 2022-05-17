package com.xiamu.item.controller;

import com.xiamu.common.pojo.PageResult;
import com.xiamu.item.pojo.Brand;
import com.xiamu.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",required = false)Boolean desc,
            @RequestParam(value = "key",required = false)String key
    ){
        PageResult<Brand> brandpage=this.brandService.queryBrandByPage(page,rows,sortBy,desc,key);
        if(CollectionUtils.isEmpty(brandpage.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brandpage);

    }
    @PostMapping
    public ResponseEntity<Void> addBrands(Brand brand, @RequestParam("cids")List<Long> cids){
        this.brandService.addBrands(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCId(@PathVariable("cid")Long cid){
        List<Brand> brands = this.brandService.queryBrandByCid(cid);
        if(brands==null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@RequestParam("id")Long id){
        Brand brand = this.brandService.queryBrandById(id);
        if(brand==null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}
