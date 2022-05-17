package com.xiamu.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiamu.common.pojo.PageResult;
import com.xiamu.item.bo.SpuBo;
import com.xiamu.item.mapper.*;
import com.xiamu.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public PageResult<SpuBo> querySpuByPage(String key, Integer page, Integer row, Boolean saleable) {
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(saleable!= null){
            criteria.andEqualTo("saleable",saleable);
        }
        PageHelper.startPage(page,row);
        List<Spu> spus = this.spuMapper.selectByExample(example);

        PageInfo<Spu> pageInfo=new PageInfo<>(spus);
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names,"-"));
            return spuBo;
        }).collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(), spuBos);
    }

    @Transactional
    public void saveGoods(SpuBo spuBo) {
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);
        saveSkuandStock(spuBo);
        sendMessage(spuBo.getId(),"insert");
    }

    public void saveSkuandStock(SpuBo spuBo){
        spuBo.getSkus().forEach(sku->{
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);
            Stock stock =new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    public SpuDetail querySpuDetailBySpuId(Long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    public List<Sku> querySkusBySpuId(Long id) {
        Sku skue = new Sku();
        skue.setSpuId(id);
        List<Sku> skus = this.skuMapper.select(skue);
        skus.forEach(sku->{
            Stock stock=this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        List<Sku> skus = this.querySkusBySpuId(spuBo.getId());
        if(!CollectionUtils.isEmpty(skus)){
            skus.forEach(sku -> {
                this.stockMapper.deleteByPrimaryKey(sku.getId());
            });

            deleteSkusBySpuId(spuBo.getId());
            saveSkuandStock(spuBo);

            spuBo.setLastUpdateTime(new Date());
            spuBo.setCreateTime(null);
            spuBo.setValid(null);
            spuBo.setSaleable(null);
            this.spuMapper.updateByPrimaryKeySelective(spuBo);

            this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        }
        sendMessage(spuBo.getId(),"update");
    }

    private void deleteSkusBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        this.skuMapper.delete(sku);
    }

    @Transactional
    public void deleteGoods(SpuBo spuBo) {
        List<Sku> skus = this.querySkusBySpuId(spuBo.getId());
        if(!CollectionUtils.isEmpty(skus)) {
            skus.forEach(sku -> {
                this.stockMapper.deleteByPrimaryKey(sku.getId());
            });
            deleteSkusBySpuId(spuBo.getId());
            this.spuDetailMapper.deleteByPrimaryKey(spuBo.getId());
            this.spuMapper.deleteByPrimaryKey(spuBo.getId());
        }
    }
    public Spu querySpuById(Long id){
        return this.spuMapper.selectByPrimaryKey(id);
    }

    public Sku querySkuById(Long id) {return this.skuMapper.selectByPrimaryKey(id);
    }
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }
}
