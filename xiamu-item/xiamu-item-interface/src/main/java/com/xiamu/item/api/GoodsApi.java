package com.xiamu.item.api;

import com.xiamu.common.pojo.PageResult;
import com.xiamu.item.bo.SpuBo;
import com.xiamu.item.pojo.Sku;
import com.xiamu.item.pojo.Spu;
import com.xiamu.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("good")
public interface GoodsApi {
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);
    /**
     * 根据条件分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value="key",required = false) String key,
            @RequestParam (value="saleable",required = false) Boolean saleable,
            @RequestParam (value="page",defaultValue = "1") Integer page,
            @RequestParam (value="rows",defaultValue = "5") Integer rows
    );
    /**
     * 根据spuid查询sku集合
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id")Long spuId);
    @GetMapping("{id}")
    public Spu querySpuById(@PathVariable("id")Long id);

    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id")Long id);
}
