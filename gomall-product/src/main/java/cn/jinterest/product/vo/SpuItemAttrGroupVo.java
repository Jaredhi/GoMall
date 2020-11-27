package cn.jinterest.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class SpuItemAttrGroupVo {

    private String groupName;
    List<Attr> attrs;
}
