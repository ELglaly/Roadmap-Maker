package com.roadmap.backendapi.service.user.unit;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        LoginTest.class,
        PasswordTest.class,
        RegistrationTest.class,
        UpdateUserTest.class,
        UserSearchTest.class,
})
public class AllUserServiceTest {
}
