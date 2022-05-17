package com.xiamu.item.controller;

import com.fasterxml.jackson.databind.node.NullNode;
import com.xiamu.common.pojo.PageResult;
import com.xiamu.item.bo.SpuBo;
import com.xiamu.item.pojo.Sku;
import com.xiamu.item.pojo.Spu;
import com.xiamu.item.pojo.SpuDetail;

import com.xiamu.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodController {
    @Autowired(required = false)
    private GoodsService goodService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer row,
            @RequestParam(value = "saleable",required = false)Boolean saleable
    ){
        PageResult<SpuBo> goodsPage = this.goodService.querySpuByPage(key,page,row,saleable);
        if(goodsPage == null ||CollectionUtils.isEmpty(goodsPage.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(goodsPage);
    }
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("id") Long id){
        if(id == null ){
            return ResponseEntity.badRequest().build();
        }
        SpuDetail spuDetail = this.goodService.querySpuDetailBySpuId(id);
        if(spuDetail==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id") Long id){
        if(id == null ){
            return ResponseEntity.badRequest().build();
        }
        List<Sku> skus=this.goodService.querySkusBySpuId(id);
        if(skus==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping("goods")
    public ResponseEntity<Void> deleteGoods(@RequestBody SpuBo spuBo){
        this.goodService.deleteGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id")Long id){
        Spu spu = this.goodService.querySpuById(id);
        if(spu == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id")Long id){
        Sku sku = this.goodService.querySkuById(id);
        if (sku == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sku);
    }

}
