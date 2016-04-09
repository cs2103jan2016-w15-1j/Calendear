package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActionTest.class, DataManagerTest.class, ParserTest.class, TaskTest.class, ViewTest.class })
public class AllTests {

}
