package stu.napls.boostimauth.service;

import stu.napls.boostimauth.model.Token;

public interface TokenService {
    Token create(Token token);

    Token update(Token token);

    Token findById(long id);

    Token findByContent(String content);
}
