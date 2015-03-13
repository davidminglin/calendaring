package calendaring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

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
		String tzid = null;
		int timeStart = 0;
		int timeEnd = 0;
		boolean badInput = true;
		int startYear = 0;
		int startMonth = 0;
		int startDay = 0;
		int endYear = 0;
		int endMonth = 0;
		int endDay = 0;
		boolean isOn = true;
		Date date = new Date();
		boolean badDate = true;
		String possibleDate = null;
		boolean allDay = false;
		boolean empty;

		System.out.println("Welcome to ICS 314, Spring 2015, iCal Calendaring Project");
		System.out.println("You may choose to create a new calendar file, or edit an existing file.\n");
		do {
			System.out.println("Please enter the number of your choice:");
			System.out.println("1) Create New Calendar");
			System.out.println("2) Use Existing Calendar");
			System.out.println("0) Quit");

			Scanner keyboard = new Scanner(System.in);
			do {
				empty = true;
				do {
					options = keyboard.nextLine();
					if(!options.isEmpty()) empty = false;
				}
				while(empty);
				choice = options.charAt(0);
				switch(choice) {

				// Create New Calendar
				case '1': 
					badInput = false;
					System.out.println("Name the calender");
					String name;
					empty = true;
					do {
						name = keyboard.nextLine();
						if(!name.isEmpty()) empty = false;
					}
					while(empty);
					System.out.println("Created " + name + ".ics file.");
					writer = new FileWriter(name + ".ics");
					newCalendar = true;
					break;

					// Use Existing Calendar
				case '2': 
					badInput = false;
					do {
						System.out.println("Please enter the file path of an existing calendar file:");
						empty = true;
						do {
							filepath = keyboard.nextLine();
							if(!filepath.isEmpty()) empty = false;
						}
						while(empty);
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
					isOn = false;
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
				empty = true;
				do {
					options = keyboard.nextLine();
					if(!options.isEmpty()) empty = false;
				}
				while(empty);
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
			String location;
			empty = true;
			do {
				location = keyboard.nextLine();
				if(!location.isEmpty()) empty = false;
			}
			while(empty);


			// Priority (3.8.1.9)
			// EX: PRIORITY:1

			badInput = true;
			do{
				System.out.println("Please enter a number corresponding to the priority for this event.");
				System.out.println("(First digit will be used)");
				System.out.println("0) No Priority");
				System.out.println("1 - 4) High Priority");
				System.out.println("5) Medium Priority");
				System.out.println("6 - 9) Low Priority");
				empty = true;
				do {
					options = keyboard.nextLine();
					if(!options.isEmpty()) empty = false;
				}
				while(empty);
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
			do{
				System.out.println("Please enter a summary for this event");
				// System.out.println("Your summary does not have to be a long one.");
				// System.out.println("For example: Department Party.");
				summary = keyboard.nextLine();
				if(summary.isEmpty()) {
					System.out.println("Error: Summary is Empty");
				}
				else badInput = false;
			}
			while(badInput);

			//Coming back to file writing stuff later

			// DTSTART (3.8.2.4)
			// EX: DTSTART:19980118T073000Z
			
			do {
				possibleDate = "";
				try {
					badInput = true;
					do {
						System.out.println("Please enter the starting month for this event.");
						empty = true;
						do {
							options = keyboard.nextLine();
							if(!options.isEmpty()) empty = false;
						}
						while(empty);
						if(!options.isEmpty()) badInput = false;
						startMonth = Integer.parseInt(options);
						possibleDate = possibleDate + startMonth;
					}
					while(badInput);
					
					do {
						System.out.println("Please enter the starting day for this event.");
						empty = true;
						do {
							options = keyboard.nextLine();
							if(!options.isEmpty()) empty = false;
						}
						while(empty);
						if(!options.isEmpty()) badInput = false;
						startDay = Integer.parseInt(options);
						possibleDate = possibleDate + "/" + startDay;
					}
					while(badInput);
					
					do {
						System.out.println("Please enter the starting year for this event.");
						empty = true;
						do {
							options = keyboard.nextLine();
							if(!options.isEmpty()) empty = false;
						}
						while(empty);
						if(!options.isEmpty()) badInput = false;
						startYear = Integer.parseInt(options);
						possibleDate = possibleDate + "/" + startYear;
					}
					while(badInput);
					
					if(dateExists(possibleDate)) {
						badDate = false;
					}
					else System.out.println("Invalid Date: " + possibleDate);
				} catch (Exception e) {
				    System.out.println("Error: Not a number!");
				}
			}
			while(badDate);
			
			badInput = true;
			do {
				System.out.println("Please enter a number corresponding to event duration");
				System.out.println("1) All day");
				System.out.println("2) Duration");
				empty = true;
				do {
					options = keyboard.nextLine();
					if(!options.isEmpty()) empty = false;
				}
				while(empty);
				choice = options.charAt(0);
				switch(choice) {
				case '1': 
					badInput = false;
					allDay = true;

					break;
					
				case '2': 
					badInput = false;

					break;
				default: System.out.println("Bad Input");
					break;
				}

			}
			while(badInput);
			
			//Z is zulu/UTC time...might need conversion; not everyone is familiar with UTC
			//But for now:

			// DTEND (3.8.2.2)
			// EX: DTEND:19960401T150000Z
			// EX2: DTEND;VALUE=DATE:19980704
			
			endYear = startYear;
			endMonth = startMonth;
			if(allDay) {
				endDay = startDay + 1;
			}
			else {
				badInput = true;
				do {
					System.out.println("Please enter the ending day for this event.");
					try {
						endDay = keyboard.nextInt();
					} catch (Exception e) {
					    System.out.println("Error: Not a number!");
					}
					if(endDay < startDay) System.out.println("Error: Ending before starting");
					else badInput = false;
				}
				while(badInput);
				
				// Get starting time
				badInput = true;
				do {
					System.out.println("Please enter the starting time [in a 24 hour clock format (hhmmss)] for this event.");
					System.out.println("Example: 083000");
					try {
						options = keyboard.nextLine();
						if(timeExists(options)) {
							badInput = false;
							timeStart = Integer.parseInt(options);
						}
						else System.out.println("Invalid Time: " + options);
							
					} catch (Exception e) {
					    System.out.println("Error: Not a number!");
					}
				}
				while(badInput);
				
				// Get ending time
				badInput = true;
				do {
					System.out.println("Please enter the ending time [in a 24 hour clock format (hhmmss)] for this event.");
					System.out.println("Example: 093000");
					try {
						options = keyboard.nextLine();
						if(timeExists(options)) {
							badInput = false;
							timeEnd = Integer.parseInt(options);
							if(timeEnd < timeStart) {
								badInput = true;
								System.out.println("Error: Time ending before starting: " + options);
							}
						}
						else System.out.println("Invalid Time: " + options);
							
					} catch (Exception e) {
					    System.out.println("Error: Not a number!");
					}
				}
				while(badInput);
			}
			
			if(allDay) {
				dateStart = startYear + "" + startMonth + "" + startDay;
				dateEnd = endYear + "" + endMonth + "" + endDay;
			}
			else {
				dateStart = startYear + "" + startMonth + "" + startDay + "T" + timeStart + "Z";
				dateEnd = endYear + "" + endMonth + "" + endDay + "T" + timeEnd + "Z";
			}
			

			// Time zone identifier (3.8.3.1, and whatever other sections you need
			// to be
			// able to specify time zones)
			// EX: TZID:/example.org/America/New_York
			badInput = true;
			do{
				System.out.println("todo");
				empty = true;
				do {
					options = keyboard.nextLine();
					if(!options.isEmpty()) empty = false;
				}
				while(empty);
				choice = options.charAt(0);

				if(choice >= '0' && choice <= '9') {
					priority = choice;
					badInput = false;
				}
				else System.out.println("Bad Input: " + choice);
			}
			while(badInput);

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
		while(isOn);
	}
	
	/**
	 * Reference: http://stackoverflow.com/questions/4516572/checking-if-a-date-exists-or-not
	 * @author user467871
	 */
	private static boolean dateExists(String date) {
        String formatString = "MM/dd/yyyy";

        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(date);
        } catch (ParseException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
	
	private static boolean timeExists(String time) {
		String formatString = "HHmmss";
		
		try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(time);
        } catch (ParseException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
	}
}
