package com.rmit.onyx2;

import com.rmit.onyx2.service.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {EmailServiceTest.class,
                TaskServiceTest.class,
                UserServiceTest.class,
                WorkspaceServiceTest.class,
                WorkspaceListServiceTest.class
        }
)
public class TestSuitService {
}
