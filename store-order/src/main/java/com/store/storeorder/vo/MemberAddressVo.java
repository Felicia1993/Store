package com.store.storeorder.vo;

import lombok.Data;

@Data
public class MemberAddressVo {
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
