package com.store.storeproduct.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * ÉÌÆ·Èý¼¶·ÖÀà
 * 
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-09 23:09:10
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ·ÖÀàid
	 */
	@TableId
	private Long catId;
	/**
	 * ·ÖÀàÃû³Æ
	 */
	private String name;
	/**
	 * ¸¸·ÖÀàid
	 */
	private Long parentCid;
	/**
	 * ²ã¼¶
	 */
	private Integer catLevel;
	/**
	 *
	 */
	private Integer showStatus;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 计量单位
	 */
	private String productUnit;
	/**
	 * 商品数量
	 */
	private Integer productCount;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<CategoryEntity> children;
}
