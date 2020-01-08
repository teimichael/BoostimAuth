package stu.napls.boostimauth.service.impl;

import org.springframework.stereotype.Service;
import stu.napls.boostimauth.model.Token;
import stu.napls.boostimauth.repository.TokenRepository;
import stu.napls.boostimauth.service.TokenService;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Author Tei Michael
 * @Date 12/29/2019
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private TokenRepository tokenRepository;

    @Override
    public Token create(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Token update(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Token findById(long id) {
        Token token = null;
        Optional<Token> result = tokenRepository.findById(id);
        if (result.isPresent()) {
            token = result.get();
        }
        return token;
    }

    @Override
    public Token findByContent(String content) {
        return tokenRepository.findByContent(content);
    }
}
