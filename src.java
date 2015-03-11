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
		char priority = 0;
		String summary = null;
		String dateStart = null;
		String dateEnd = null;
		boolean badInput = true;
		int startYear = 0;
		int startMonth = 0;
		int startDay = 0;
		int endYear = 0;
		int endMonth = 0;
		int endDay = 0;
		
		System.out.println("Welcome to ICS 314, Spring 2015, iCal Calendaring Project");
		System.out.println("You may choose to create a new calendar file, or edit an existing file.\n");
		System.out.println("Please enter the number of your choice:");
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
					System.out.println("Created calendar.ics file.");
					writer = new FileWriter("calendar.ics");
					newCalendar = true;
					break;
					
				// Use Existing Calendar
				case '2': 
					badInput = false;
					do {
						System.out.println("Please enter the file path of an existing calendar file:");
						filepath = keyboard.nextLine();
						file = new File(filepath);
						if(file.exists()) {
							break;
						}
						else System.out.println("Sorry, the path you entered is invalid.");
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
					
				default: System.out.println("Bad Input: " + choice);
					break;
			}
				
		}
		while(badInput);
		
		// Version (section 3.7.4 of RFC 5545)
		// EX: VERSION:2.0
		
		// Classification (3.8.1.3). Note this is a way of users designating
		// events as public (default), private, or confidential.
		// classvalue = "PUBLIC" / "PRIVATE" / "CONFIDENTIAL" 
		// EX: CLASS:PUBLIC
		badInput = true;
		do {
			System.out.println("Please enter a number corresponding to how you'd like to classify your event:");
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
		
		// Location (3.8.1.7)
		// EX: LOCATION:Conference Room - F123\, Bldg. 002
		// EX2: LOCATION;ALTREP="http://xyzcorp.com/conf-rooms/f123.vcf":
		// Conference Room - F123\, Bldg. 002
		System.out.println("Enter Event Location:");
		String location = keyboard.nextLine();
		

		// Priority (3.8.1.9)
		// EX: PRIORITY:1
		
		badInput = true;
		do{
			System.out.println("Please enter a number corresponding to the priority for this event.");
			System.out.println("(First number will be used)");
			System.out.println("0) No Priority");
			System.out.println("1 - 4) High Priority");
			System.out.println("5) Medium Priority");
			System.out.println("6 - 9) Low Priority");
			options = keyboard.nextLine();
			choice = options.charAt(0);
			
			if(choice >= '0' && choice <= '9') {
				priority = choice;
				badInput = false;
			}
			else System.out.println("Bad Input: " + choice);
		}
		while(badInput);
		//Coming back to the file writing stuff later

		// Summary (3.8.1.12)
		// EX: SUMMARY:Department Party
		
		badInput = true;
		
		System.out.println("Please enter a summary for this event");
		// System.out.println("Your summary does not have to be a long one.");
		// System.out.println("For example: Department Party.");
		summary = keyboard.nextLine();
		
		//Coming back to file writing stuff later

		// DTSTART (3.8.2.4)
		// EX: DTSTART:19980118T073000Z
		
		// Have to figure out handling input mismatch exception for each one 
		System.out.println("Please enter the starting year for this event.");
		startYear = keyboard.nextInt();
		System.out.println("Please enter the starting month for this event.");
		startMonth = keyboard.nextInt();
		System.out.println("Please enter the starting day for this event.");
		startDay = keyboard.nextInt();
		
		//Z is zulu/UTC time...might need conversion; not everyone is familiar with UTC
		//But for now:
		dateStart = (startYear + "" + startMonth + "" + startDay);

		// DTEND (3.8.2.2)
		// EX: DTEND:19960401T150000Z
		// EX2: DTEND;VALUE=DATE:19980704
		
		// Also have to figure out handling input mismatch exception for each one here
		System.out.println("Please enter the ending year for this event.");
		endYear = keyboard.nextInt();
		System.out.println("Please enter the ending month for this event.");
		endMonth = keyboard.nextInt();
		System.out.println("Please enter the ending day for this event.");
		endDay = keyboard.nextInt();
		
		//Same issue with Z/UTC time as in dtstart
		
		dateEnd = (endYear + "" + endMonth + "" + endDay);

		// Time zone identifier (3.8.3.1, and whatever other sections you need
		// to be
		// able to specify time zones)
		// EX: TZID:/example.org/America/New_York
		
		
		if(newCalendar) {
			writer.write(beginCalendar);
			writer.write(version);
		}
		
		writer.write(beginEvent);
		writer.write(classification);
		writer.write("LOCATION:" + location + "\n");
		writer.write("PRIORITY:" + priority + "\n");
		writer.write("SUMMARY:" + summary + "\n");
		writer.write("DTSTART:" + dateStart + "\n");
		writer.write("DTEND:" + dateEnd + "\n");
		writer.write(endEvent);
		writer.write(endCalendar);
		writer.close();
	}

}
