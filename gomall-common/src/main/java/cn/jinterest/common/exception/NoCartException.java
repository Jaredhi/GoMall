package cn.jinterest.common.exception;

import lombok.Getter;
import lombok.Setter;


public class NoCartException extends RuntimeException {


    public NoCartException() {
        super("购物车无商品！");
    }

    public NoCartException(String msg) {
        super(msg);
    }


}
