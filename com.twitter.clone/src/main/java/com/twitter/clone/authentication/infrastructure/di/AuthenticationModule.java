package com.twitter.clone.authentication.infrastructure.di;


import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.twitter.clone.authentication.api.controller.AuthenticationController;
import com.twitter.clone.authentication.api.mapper.AuthenticationMapper;
import com.twitter.clone.authentication.api.mapper.UserMapper;
import com.twitter.clone.authentication.api.servcie.ICookieService;
import com.twitter.clone.authentication.api.servcie.IUserService;
import com.twitter.clone.authentication.data.dau.IUserDAO;
import com.twitter.clone.authentication.data.repository.UserRepository;
import com.twitter.clone.authentication.domain.repository.IUserRepository;
import com.twitter.clone.authentication.domain.service.CookieService;
import com.twitter.clone.authentication.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class AuthenticationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IUserRepository.class).to(UserRepository.class);
        bind(IUserService.class).to(UserService.class);
        bind(ICookieService.class).to(CookieService.class);
        bind(AuthenticationController.class);
        bind(UserMapper.class);
        bind(AuthenticationMapper.class);
    }

    @Provides
    @Singleton
    public IUserDAO provideUserDau(Jdbi jdbi) {
        return jdbi.onDemand(IUserDAO.class);
    }
}
