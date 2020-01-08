package stu.napls.boostimauth.service;

import stu.napls.boostimauth.model.Identity;

public interface IdentityService {
    Identity update(Identity identity);

    Identity findByUsername(String username);

    Identity findByUuid(String uuid);
}
