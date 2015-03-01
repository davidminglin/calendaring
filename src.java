package calendaring;

public class src {

	public static void main(String[] args) {
		System.out.println("Calendar");
		
		// Version	(section	3.7.4	of	RFC	5545)	
		// EX: VERSION:2.0
		
		
		
		// Classification (3.8.1.3). Note this	is a way of users designating events	as	
		// public (default), private, or confidential.
		// EX: CLASS:PUBLIC
		
		
		
		// Location	(3.8.1.7)
		// EX: LOCATION:Conference Room - F123\, Bldg. 002
		// EX2: LOCATION;ALTREP="http://xyzcorp.com/conf-rooms/f123.vcf":
        	// 	Conference Room - F123\, Bldg. 002
		
		
		
		// Priority	(3.8.1.9)
		// EX: PRIORITY:1
		
		
		
		// Summary	(3.8.1.12)	
		// EX: SUMMARY:Department Party
		
		
		
		// DTSTART	(3.8.2.4)
		// EX: DTSTART:19980118T073000Z
		
		
		// DTEND	(3.8.2.2)	
		// EX: DTEND:19960401T150000Z
		// EX2: DTEND;VALUE=DATE:19980704
		
		
		// Time	zone identifier	(3.8.3.1, and whatever other sections you need to be	
		//	able to specify time zones)
		// EX: TZID:/example.org/America/New_York
	}

}
