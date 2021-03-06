package com.mparticle.identity;

public interface MParticleIdentityClient {
    IdentityHttpResponse login(IdentityApiRequest request) throws Exception;
    IdentityHttpResponse logout(IdentityApiRequest request) throws Exception;
    IdentityHttpResponse identify(IdentityApiRequest request) throws Exception;
    IdentityHttpResponse modify(IdentityApiRequest request) throws Exception;
}
