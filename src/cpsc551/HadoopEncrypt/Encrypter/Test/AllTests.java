package cpsc551.HadoopEncrypt.Encrypter.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all unit tests for the cpsc551.HadoopEncrypt.Encrypter package
 * @author Chad Wyszynski
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ BlockerTest.class, EncrypterTest.class, IntegrationTest.class })
public class AllTests {

}
