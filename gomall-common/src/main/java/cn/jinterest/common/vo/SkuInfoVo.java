package cn.jinterest.common.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class SkuInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long skuId;
	private String skuName;

}
