package stu.napls.boostimauth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import stu.napls.boostimauth.config.GlobalKey;
import stu.napls.boostimauth.core.dictionary.TokenConst;
import stu.napls.boostimauth.core.exception.Assert;
import stu.napls.boostimauth.core.response.Response;
import stu.napls.boostimauth.model.Token;
import stu.napls.boostimauth.model.vo.AuthVerify;
import stu.napls.boostimauth.service.TokenService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author Tei Michael
 * @Date 12/29/2019
 */
@RestController
public class VerifyController {

    @Resource
    private TokenService tokenService;

    @PostMapping("/verify")
    public Response verifyToken(@RequestBody AuthVerify authVerify) {
        String token = authVerify.getToken();

        // Validate
        Assert.notNull(token, "No token information.");
        Assert.isTrue(!"".equals(token), "Token is empty.");

        // Parse JWT
        Claims claims = Jwts.parser()
                .setSigningKey(GlobalKey.JWT_SIGNING_KEY)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();


        Assert.isTrue(claims.getExpiration().after(new Date()), "Token expired.");

        Token t = tokenService.findByContent(token);
        Assert.notNull(t, "Token is invalid");
        Assert.isTrue(t.getStatus() == TokenConst.NORMAL, "Token expired.");

        return Response.success("Verified.", claims.getSubject());
    }
}
