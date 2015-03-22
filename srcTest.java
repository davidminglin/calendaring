package calendaring;

import static org.junit.Assert.*;

import org.junit.Test;

public class srcTest {
	src s = new src();
	
	@Test
	public void testGetTime() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDateExists() {
		assertTrue(s.dateExists("01/20/2015"));
		assertFalse(s.dateExists("00/33/2015"));
		assertFalse(s.dateExists("00/33/0000"));
	}
	
	@Test
	public void testTimeExists() {
		assertFalse(s.timeExists("137000"));
	}
}
