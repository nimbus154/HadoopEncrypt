package cpsc551.HadoopEncrypt.MapReduce.Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MiscTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		byte b = (byte) 0xb9;
		int temp = Integer.parseInt("b9", 16);
		byte parsed = (byte) temp;
		assertEquals(b, parsed);		
	}

}
