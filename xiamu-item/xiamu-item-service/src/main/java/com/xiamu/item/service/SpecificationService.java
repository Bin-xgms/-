package com.xiamu.item.service;

import com.xiamu.item.mapper.SpecGroupMapper;
import com.xiamu.item.mapper.SpecParamMapper;
import com.xiamu.item.pojo.SpecGroup;
import com.xiamu.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired(required = false)
    private SpecGroupMapper specGroupMapper;
    @Autowired(required = false)
    private SpecParamMapper specParamMapper;
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);

        return this.specParamMapper.select(specParam);

    }

    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> select = this.specGroupMapper.select(specGroup);
        return select;
    }

    public List<SpecGroup> queryGroupsWithParamByCid(Long cid) {
        List<SpecGroup> groups = this.querySpecGroupsByCid(cid);
        groups.forEach(group ->{
            List<SpecParam> params = this.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
    }
}
