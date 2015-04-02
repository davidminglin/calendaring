package calendaring;

import static org.junit.Assert.*;

import org.junit.Test;

public class srcTest {
	src s = new src();
	
	@Test
	public void testPad0() {
		assertEquals("000005", s.pad0(5));
		assertEquals("123456", s.pad0(123456));
	}
	@Test
	public void testGetTime() {
		s.getTime("DTSTART:20150508T230000");
		s.getTime("DTEND:20150508T2359059");
		assertTrue(s.times.size() == 2);
		assertTrue(s.times.get(0).equals("230000"));
		assertTrue(s.times.get(1).equals("2359059"));
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
