package com.xiamu.item.controller;

import com.xiamu.item.pojo.SpecGroup;
import com.xiamu.item.pojo.SpecParam;
import com.xiamu.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecificationController {
    @Autowired(required = false)
    private SpecificationService specificationService;
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "generic",required = false)Boolean generic,
            @RequestParam(value = "search",required = false)Boolean searching){
        List<SpecParam> specParams=this.specificationService.queryParams(gid,cid,generic,searching);
        if(CollectionUtils.isEmpty(specParams)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specParams);
    }
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupByCid(@PathVariable("cid")Long cid){
        List<SpecGroup> specGroups= this.specificationService.querySpecGroupsByCid(cid);
        if(specGroups==null||specGroups.size()==0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid")Long cid){
        List<SpecGroup> groups=this.specificationService.queryGroupsWithParamByCid(cid);
        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }
}
