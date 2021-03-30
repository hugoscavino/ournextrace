package com.ijudy.races.service;

import com.ijudy.races.service.email.PostMasterService;
import com.ijudy.races.service.security.CustomUserDetailsService;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @MockBean
    public CustomUserDetailsService customUserDetailsService;

    @MockBean
    public PostMasterService postMasterService;

}
