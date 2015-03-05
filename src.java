package calendaring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class src {

	public static void main(String[] args) throws IOException {
		FileWriter writer = null;
		File file = null;
		String options;
		String filepath = null;
		char choice;
		boolean newCalendar = false;
		String beginCalendar = "BEGIN:VCALENDAR\n";
		String endCalendar = "END:VCALENDAR";
		String version = "VERSION:2.0\n";
		String beginEvent = "BEGIN:VEVENT\n";
		String endEvent = "END:VEVENT\n";
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
					writer = new FileWriter("calendar.ics");
					newCalendar = true;
					break;
					
				// Use Existing Calendar
				case '2': 
					badInput = false;
					do {
						System.out.println("Enter File Path of Existing Calendar File:");
						filepath = keyboard.nextLine();
						file = new File(filepath);
						if(file.exists()) {
							break;
						}
						else System.out.println("Bad File Path");
					}
					while(!file.exists());
					
					// Removes the END:VCALENDAR
					// Source Referenced: http://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it
					File temp = new File("temp.ics");
					BufferedReader bReader = new BufferedReader(new FileReader(file));
					BufferedWriter bWriter = new BufferedWriter(new FileWriter(temp));
					String line;
					while((line = bReader.readLine()) != null) {
						if (!line.trim().equals(endCalendar)) {
							bWriter.write(line + System.getProperty("line.separator"));
					    }
					}
					bReader.close();
					bWriter.close();
					
					if (!file.delete()) {
						System.out.println("Not able to delete original");
				        return;
				    }
					if (!temp.renameTo(file)) {
				        System.out.println("Not able to rename file");
				    }
					file = new File(filepath);
					writer = new FileWriter(file, true);
					
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
		if(newCalendar) {
			writer.write(beginCalendar);
			writer.write(version);
		}
		
		writer.write(beginEvent);
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
		writer.write("LOCATION:" + location + "\n");
		
		
		writer.write(endEvent);
		writer.write(endCalendar);
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
