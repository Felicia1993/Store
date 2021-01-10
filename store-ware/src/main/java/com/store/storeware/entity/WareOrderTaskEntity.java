package com.store.storeware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ¿â´æ¹¤×÷µ¥
 * 
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:46:40
 */
@Data
@TableName("wms_ware_order_task")
public class WareOrderTaskEntity implements Serializable {
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
	 * ÊÕ»õÈË
	 */
	private String consignee;
	/**
	 * ÊÕ»õÈËµç»°
	 */
	private String consigneeTel;
	/**
	 * ÅäËÍµØÖ·
	 */
	private String deliveryAddress;
	/**
	 * ¶©µ¥±¸×¢
	 */
	private String orderComment;
	/**
	 * ¸¶¿î·½Ê½¡¾ 1:ÔÚÏß¸¶¿î 2:»õµ½¸¶¿î¡¿
	 */
	private Integer paymentWay;
	/**
	 * ÈÎÎñ×´Ì¬
	 */
	private Integer taskStatus;
	/**
	 * ¶©µ¥ÃèÊö
	 */
	private String orderBody;
	/**
	 * ÎïÁ÷µ¥ºÅ
	 */
	private String trackingNo;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * ²Ö¿âid
	 */
	private Long wareId;
	/**
	 * ¹¤×÷µ¥±¸×¢
	 */
	private String taskComment;

}
