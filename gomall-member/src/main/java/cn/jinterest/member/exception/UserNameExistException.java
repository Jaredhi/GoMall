package cn.jinterest.member.exception;


public class UserNameExistException extends RuntimeException {

    public UserNameExistException() {
        super("用户名已存在");
    }
}
