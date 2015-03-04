package calendaring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class src {

	public static void main(String[] args) throws IOException {
		FileWriter writer = new FileWriter("calendar.ics");
		File file;
		String options;
		char choice;
		String begin = "BEGIN:VCALENDAR\n";
		String version = "VERSION:2.0\n";
		String classification = null;
		boolean badInput = true;
		
		System.out.println("Calendar\n");
		System.out.println("Enter Number Option:");
		System.out.println("1) Create New Calendar");
		System.out.println("2) Use Existing Calendar");
		System.out.println("0) Quit");

		Scanner keyboard = new Scanner(System.in);
		do {
			options = keyboard.nextLine();
			choice = options.charAt(0);
				switch(choice) {
				
				// Create New Calendar
				case '1': 
					badInput = false;
					break;
					
				// Use Existing Calendar
				case '2': 
					badInput = false;
					System.out.println("Enter File Path:");
					try {
					    String filename = keyboard.nextLine();
					    writer = new FileWriter(filename, true);

					} catch(IOException e) {
					    System.err.println("Exception: " + e.getMessage());
					}
					
					break;
					
				case '0': 
					System.out.println("Closed");
					System.exit(0);
					break;
					
				default: System.out.println("Bad Input");
					break;
			}
				
		}
		while(badInput);
		
		// Version (section 3.7.4 of RFC 5545)
		// EX: VERSION:2.0
		writer.write(begin);
		writer.write(version);
		
		// Classification (3.8.1.3). Note this is a way of users designating
		// events as public (default), private, or confidential.
		// classvalue = "PUBLIC" / "PRIVATE" / "CONFIDENTIAL" 
		// EX: CLASS:PUBLIC
		badInput = true;
		do {
			System.out.println("Enter Number Option for Classification:");
			System.out.println("1) Public");
			System.out.println("2) Private");
			System.out.println("3) Confidential");
			options = keyboard.nextLine();
			choice = options.charAt(0);
			switch(choice) {
				case '1': 
					badInput = false;
					classification = "CLASS:PUBLIC\n";
					break;
					

				case '2': 
					badInput = false;
					classification = "CLASS:PRIVATE\n";
					break;
					
				case '3': 
					badInput = false;
					classification = "CLASS:CONFIDENTIAL\n";
					break;
				
				case '0': 
					System.out.println("Closed");
					System.exit(0);
					break;
					
				default: System.out.println("Bad Input");
					break;
			}
				
		}
		while(badInput);
		writer.write(classification);
		
		// Location (3.8.1.7)
		// EX: LOCATION:Conference Room - F123\, Bldg. 002
		// EX2: LOCATION;ALTREP="http://xyzcorp.com/conf-rooms/f123.vcf":
		// Conference Room - F123\, Bldg. 002
		System.out.println("Enter Event Location:");
		String location = keyboard.nextLine();
		writer.write(location);
		
		writer.close();
	
		
		
		
		
		
		

		

		// Priority (3.8.1.9)
		// EX: PRIORITY:1

		// Summary (3.8.1.12)
		// EX: SUMMARY:Department Party

		// DTSTART (3.8.2.4)
		// EX: DTSTART:19980118T073000Z

		// DTEND (3.8.2.2)
		// EX: DTEND:19960401T150000Z
		// EX2: DTEND;VALUE=DATE:19980704

		// Time zone identifier (3.8.3.1, and whatever other sections you need
		// to be
		// able to specify time zones)
		// EX: TZID:/example.org/America/New_York
	}

}
