package com.igzcode.java.gae.test.example;



public class TestManager extends TestFactory {

    private TestManager () {}
    
    static private TestManager testManager = new TestManager();
    
    static public TestManager getInstance () {
        return testManager;
    }
    
}
