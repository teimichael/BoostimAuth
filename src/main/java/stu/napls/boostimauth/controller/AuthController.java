package stu.napls.boostimauth.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.napls.boostimauth.config.GlobalKey;
import stu.napls.boostimauth.core.dictionary.ErrorCode;
import stu.napls.boostimauth.core.dictionary.IdentityConst;
import stu.napls.boostimauth.core.dictionary.TokenConst;
import stu.napls.boostimauth.core.exception.Assert;
import stu.napls.boostimauth.core.response.Response;
import stu.napls.boostimauth.model.Identity;
import stu.napls.boostimauth.model.Token;
import stu.napls.boostimauth.model.vo.AuthLogin;
import stu.napls.boostimauth.model.vo.AuthLogout;
import stu.napls.boostimauth.model.vo.AuthPreregister;
import stu.napls.boostimauth.model.vo.AuthRegister;
import stu.napls.boostimauth.service.IdentityService;
import stu.napls.boostimauth.service.TokenService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @Author Tei Michael
 * @Date 12/28/2019
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IdentityService identityService;

    @Resource
    private TokenService tokenService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/login")
    public Response login(@RequestBody AuthLogin authLogin) {
        Identity identity = identityService.findByUsername(authLogin.getUsername());
        Assert.notNull(identity, ErrorCode.USERNAME_NOT_EXIST, "Username does not exist.");
        Assert.isTrue(identity.getStatus() == IdentityConst.NORMAL, ErrorCode.ABNORMAL, "Account is not normal.");
        Assert.isTrue(bCryptPasswordEncoder.matches(authLogin.getPassword(), identity.getPassword()), ErrorCode.PASSWORD_WRONG, "Wrong password.");

        Token token = identity.getToken();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.setTime(new Date());
        // Token expiry date
        calendar.add(Calendar.HOUR, 2);
//        calendar.add(Calendar.MINUTE, 1);
        Date expiryDate = calendar.getTime();
        token.setIssuingDate(now);
        token.setExpiryDate(expiryDate);
        token.setContent("Bearer " + Jwts.builder()
                .setSubject(identity.getUuid())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, GlobalKey.JWT_SIGNING_KEY)
                .compact());
        token.setStatus(TokenConst.NORMAL);
        tokenService.update(token);
        return Response.success("Login successfully.", token.getContent());
    }

    @PostMapping("/preregister")
    public Response preRegister(@RequestBody AuthPreregister authPreregister) {
        Identity existIdentity = identityService.findByUsername(authPreregister.getUsername());

        // Whether is preregistered.
        Assert.isTrue(existIdentity == null || existIdentity.getStatus() == IdentityConst.PREREGISTER, ErrorCode.USERNAME_EXIST, "Username exists.");
        if (existIdentity != null && existIdentity.getStatus() == IdentityConst.PREREGISTER) {
            return Response.success("Preregister successfully.", existIdentity.getUuid());
        }

        Identity identity = new Identity();
        String uuid = authPreregister.getUuid();
        if (uuid == null || uuid.equals("")) {
            uuid = UUID.randomUUID().toString();
        }
        identity.setUuid(uuid);
        identity.setUsername(authPreregister.getUsername());
        identity.setPassword(bCryptPasswordEncoder.encode(authPreregister.getPassword()));
        identity.setStatus(IdentityConst.PREREGISTER);

        Token token = new Token();
        token.setStatus(TokenConst.INVALID);
        tokenService.create(token);

        identity.setToken(token);
        identityService.update(identity);
        return Response.success("Preregister successfully.", identity.getUuid());
    }

    @PostMapping("/register")
    public Response register(@RequestBody AuthRegister authRegister) {
        Identity identity = identityService.findByUuid(authRegister.getUuid());
        Assert.notNull(identity, "Register failed because UUID is missing.");
        identity.setStatus(IdentityConst.NORMAL);
        identityService.update(identity);
        return Response.success("Register successfully.", identity.getUuid());
    }

    /**
     * This request must be authenticated before calling.
     *
     * @param authLogout
     * @return
     */
    @PostMapping("/logout")
    public Response logout(@RequestBody AuthLogout authLogout) {
        Identity identity = identityService.findByUuid(authLogout.getUuid());
        Assert.notNull(identity, ErrorCode.USERNAME_NOT_EXIST, "Username does not exist.");
        Token token = identity.getToken();
        token.setStatus(TokenConst.INVALID);
        tokenService.update(token);
        return Response.success("Logout successfully.");
    }
}
