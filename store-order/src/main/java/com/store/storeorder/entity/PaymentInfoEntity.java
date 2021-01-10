package com.store.storeorder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Ö§¸¶ÐÅÏ¢±í
 * 
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:33:00
 */
@Data
@TableName("oms_payment_info")
public class PaymentInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * ¶©µ¥ºÅ£¨¶ÔÍâÒµÎñºÅ£©
	 */
	private String orderSn;
	/**
	 * ¶©µ¥id
	 */
	private Long orderId;
	/**
	 * Ö§¸¶±¦½»Ò×Á÷Ë®ºÅ
	 */
	private String alipayTradeNo;
	/**
	 * Ö§¸¶×Ü½ð¶î
	 */
	private BigDecimal totalAmount;
	/**
	 * ½»Ò×ÄÚÈÝ
	 */
	private String subject;
	/**
	 * Ö§¸¶×´Ì¬
	 */
	private String paymentStatus;
	/**
	 * ´´½¨Ê±¼ä
	 */
	private Date createTime;
	/**
	 * È·ÈÏÊ±¼ä
	 */
	private Date confirmTime;
	/**
	 * »Øµ÷ÄÚÈÝ
	 */
	private String callbackContent;
	/**
	 * »Øµ÷Ê±¼ä
	 */
	private Date callbackTime;

}
