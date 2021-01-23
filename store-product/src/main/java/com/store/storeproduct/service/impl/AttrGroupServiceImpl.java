package com.store.storeproduct.service.impl;

import com.store.storeproduct.vo.SkuItemVo;
import com.store.storeproduct.vo.SpuItemAttrGroupVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeproduct.dao.AttrGroupDao;
import com.store.storeproduct.entity.AttrGroupEntity;
import com.store.storeproduct.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        } else {
            String key = (String)params.get("key");
            //select * from pms_attr_group where catelogId=? and (attr_group_id=key or attr_group_name like %key%)
            QueryWrapper<AttrGroupEntity> objectQueryWrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
            if (!StringUtils.isEmpty(key)) {
                objectQueryWrapper.and(obj->{
                    obj.eq("attr_group_id", key).or().like("attr_group_name",key );
                });
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    objectQueryWrapper);
            return new PageUtils(page);
        }

    }

    /**
     * 查出当前spu对应的所有属性的分组信息以及当前分组下的所有属性对应的值
     *
     * @param id
     * @param categoryId
     * @return
     */
    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long id, Long categoryId) {
        //select ag.attr_group_name,  ag.attr_group_id from pms_attr_group ag
        // left join pms_attr_attrgroup_relation aar on
        // aar.attr_group_id=ag.attr_id
        // left join pms_attr attr on attr.attr_id=arr.attr_id
        // where catelog_id=225
        AttrGroupDao baseMapper = this.getBaseMapper();
        List<SpuItemAttrGroupVo> vos = baseMapper.getAttrgetAttrGroupWithAttrsBySpuId(id,categoryId );
        return null;
    }
}