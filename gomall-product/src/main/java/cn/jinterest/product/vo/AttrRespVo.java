package cn.jinterest.product.vo;


import lombok.Data;

/**
 * @Auther: AJun
 * @version:1.0
 * @Date: 2020/11/07  13:18
 * @Description:
 */
@Data
public class AttrRespVo extends AttrVo {
    //所属分类名字
    private String catelogName;
    //所属分组名字
    private String groupName;
    //所属分类路径
    private Long[] catelogPath;

}
