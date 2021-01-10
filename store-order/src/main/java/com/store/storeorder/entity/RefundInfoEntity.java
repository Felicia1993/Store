package com.store.storeorder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ÍË¿îÐÅÏ¢
 * 
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:33:00
 */
@Data
@TableName("oms_refund_info")
public class RefundInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * ÍË¿îµÄ¶©µ¥
	 */
	private Long orderReturnId;
	/**
	 * ÍË¿î½ð¶î
	 */
	private BigDecimal refund;
	/**
	 * ÍË¿î½»Ò×Á÷Ë®ºÅ
	 */
	private String refundSn;
	/**
	 * ÍË¿î×´Ì¬
	 */
	private Integer refundStatus;
	/**
	 * ÍË¿îÇþµÀ[1-Ö§¸¶±¦£¬2-Î¢ÐÅ£¬3-ÒøÁª£¬4-»ã¿î]
	 */
	private Integer refundChannel;
	/**
	 * 
	 */
	private String refundContent;

}
