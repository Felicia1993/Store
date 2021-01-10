package com.store.storemember.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * »áÔ±ÊÕ»õµØÖ·
 * 
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:08:59
 */
@Data
@TableName("ums_member_receive_address")
public class MemberReceiveAddressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * member_id
	 */
	private Long memberId;
	/**
	 * ÊÕ»õÈËÐÕÃû
	 */
	private String name;
	/**
	 * µç»°
	 */
	private String phone;
	/**
	 * ÓÊÕþ±àÂë
	 */
	private String postCode;
	/**
	 * Ê¡·Ý/Ö±Ï½ÊÐ
	 */
	private String province;
	/**
	 * ³ÇÊÐ
	 */
	private String city;
	/**
	 * Çø
	 */
	private String region;
	/**
	 * ÏêÏ¸µØÖ·(½ÖµÀ)
	 */
	private String detailAddress;
	/**
	 * Ê¡ÊÐÇø´úÂë
	 */
	private String areacode;
	/**
	 * ÊÇ·ñÄ¬ÈÏ
	 */
	private Integer defaultStatus;

}
