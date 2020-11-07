package cn.jinterest.product.entity;

import cn.jinterest.common.valid.AddGroup;
import cn.jinterest.common.valid.ListValue;
import cn.jinterest.common.valid.UpdateGroup;
import cn.jinterest.common.valid.UpdateStatusGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "修改时必须携带品牌id", groups = {UpdateGroup.class, UpdateStatusGroup.class})
	@Null(message = "添加时品牌id必须为空", groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名必须提交", groups = {UpdateGroup.class,AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "新增时品牌图片不能为空",groups = {AddGroup.class})
	@URL(message = "必须是合法的url地址",groups = {UpdateGroup.class,AddGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue(vals= {0,1},message = "必须是1 or 0",groups = {UpdateGroup.class,AddGroup.class, UpdateStatusGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个a-z或A-Z的字母",groups = {UpdateGroup.class,AddGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空",groups = {UpdateGroup.class,AddGroup.class})
	@Min(value = 0,message = "排序必须大于等于0", groups = {UpdateGroup.class,AddGroup.class})
	private Integer sort;

}
