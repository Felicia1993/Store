package com.store.storemember.exception;

public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("用户名存在");
    }
}
