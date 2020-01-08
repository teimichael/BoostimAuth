package stu.napls.boostimauth.service.impl;

import org.springframework.stereotype.Service;
import stu.napls.boostimauth.model.Identity;
import stu.napls.boostimauth.repository.IdentityRepository;
import stu.napls.boostimauth.service.IdentityService;

import javax.annotation.Resource;

/**
 * @Author Tei Michael
 * @Date 12/29/2019
 */
@Service("identityService")
public class IdentityServiceImpl implements IdentityService {

    @Resource
    private IdentityRepository identityRepository;

    @Override
    public Identity update(Identity identity) {
        return identityRepository.save(identity);
    }

    @Override
    public Identity findByUsername(String username) {
        return identityRepository.findByUsername(username);
    }

    @Override
    public Identity findByUuid(String uuid) {
        return identityRepository.findByUuid(uuid);
    }
}
