package com.ijudy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("integration")
public abstract class SpringIntegrationTest {

   @MockBean
   ClientRegistrationRepository clientRegistrationRepository;
}
