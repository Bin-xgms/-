package com.xiamu.goods.service;

import com.xiamu.goods.client.BrandClient;
import com.xiamu.goods.client.CategoryClient;
import com.xiamu.goods.client.GoodsClient;
import com.xiamu.goods.client.SpecificationClient;
import com.xiamu.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsClient goodsClient;

    public Map<String,Object> loadData(Long id){
        Map<String, Object> model = new HashMap<>();
        Spu spu = this.goodsClient.querySpuById(id);
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(id);
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNameByIds(cids);
        List<Map<String,Object>>categories=new ArrayList<>();
        for(int i =0;i<cids.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }
        List<Sku> skus = this.goodsClient.querySkusBySpuId(id);
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParamByCid(spu.getCid3());
        //查询特殊的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        //初始化特殊规格参数的map
        Map<Long,String> paramMap=new HashMap<>();
        params.forEach(param ->{
            paramMap.put(param.getId(),param.getName());
        });
        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories); //分类id与分类名对应的List<Map<String,Object>>categories
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);//带着具体参数List<SpecParam> params的参数组
        model.put("paramMap",paramMap);//只含特殊参数id和name的特殊参数map
        return model;
    }
}
