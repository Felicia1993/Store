package com.store.storeorder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ¶©µ¥ÏîÐÅÏ¢
 * 
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:33:00
 */
@Data
@TableName("oms_order_item")
public class OrderItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * order_id
	 */
	private Long orderId;
	/**
	 * order_sn
	 */
	private String orderSn;
	/**
	 * spu_id
	 */
	private Long spuId;
	/**
	 * spu_name
	 */
	private String spuName;
	/**
	 * spu_pic
	 */
	private String spuPic;
	/**
	 * spu品牌
	 */
	private String spuBrand;
	/**
	 * 三级目录ID
	 */
	private Long categoryId;
	/**
	 * 商品skuId
	 */
	private Long skuId;
	/**
	 * 商品sku名称
	 */
	private String skuName;
	/**
	 * 商品sku图片
	 */
	private String skuPic;
	/**
	 * 商品sku价格
	 */
	private BigDecimal skuPrice;
	/**
	 * 商品购买数量
	 */
	private Integer skuQuantity;
	/**
	 * É商品销售属性组合（JSON）
	 */
	private String skuAttrsVals;
	/**
	 * 商品促销分解金额
	 */
	private BigDecimal promotionAmount;
	/**
	 * 优惠券优惠分解金额
	 */
	private BigDecimal couponAmount;
	/**
	 * 积分优惠分解金额
	 */
	private BigDecimal integrationAmount;
	/**
	 * ¸ÃÉÌÆ·¾­¹ýÓÅ»ÝºóµÄ·Ö½â½ð¶î
	 */
	private BigDecimal realAmount;
	/**
	 * ÔùËÍ»ý·Ö
	 */
	private Integer giftIntegration;
	/**
	 * ÔùËÍ³É³¤Öµ
	 */
	private Integer giftGrowth;

}
