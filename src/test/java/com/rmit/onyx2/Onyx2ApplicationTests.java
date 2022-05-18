package com.rmit.onyx2;

import org.junit.jupiter.api.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Onyx2ApplicationTests {
    public static void main(String[] args) {
        Result testResult = JUnitCore.runClasses(TestSuitService.class);
        System.out.println("-----------------------------------------------");
        System.out.println("Testing Services report: ");
        System.out.println("Number of test runs: " + testResult.getRunCount());
        System.out.println("Number of Assumption failure: " + testResult.getAssumptionFailureCount());
        System.out.println("Number of Ignore case: " + testResult.getIgnoreCount());
    }
}
