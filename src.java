package calendaring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.PatternSyntaxException;

/**
* Class that implements the Calendaring assignment (based on RFC 5545) for ICS 314 (Spring 2015). 
* Allows someone to: 
* 1. Create a new .ics calendar file. 
* 2. Read in a bunch of .ics files to highlight free times for one person.
* 3. Read in .ics files for two separate people to highlight possible meeting times.
*
* @Author David Lin.
* @Co-Author Anthony Chang.
* @Co-Author Leon Liu.
* 
*/

public class src {
	static FileWriter writer = null;
	static File file = null;
	static String options;
	static String filepath = null;
	static char choice;
	static boolean newCalendar = false;
	static String beginCalendar = "BEGIN:VCALENDAR\n";
	static String endCalendar = "END:VCALENDAR";
	static String version = "VERSION:2.0\n";
	static String beginEvent = "BEGIN:VEVENT\n";
	static String endEvent = "END:VEVENT\n";
	static String classification = null;
	static String location = null;
	static char priority = 0;
	static String summary = null;
	static String dateStart = null;
	static String dateEnd = null;
	static String tzid = null;
	static String timeStart = null;
	static String timeEnd = null;
	static boolean badInput = true;
	static String startYear = null;
	static String startMonth = null;
	static String startDay = null;
	static String endYear = null;
	static String endMonth = null;
	static String endDay = null;
	static boolean isOn = true;
	static Date date = new Date();
	static boolean badDate = true;
	static String possibleDate = null;
	static boolean allDay = false;
	static boolean empty = true;
	static int check = 0;
	static char start = 0;
	static Scanner keyboard = new Scanner(System.in);
	static ArrayList<String> times = new ArrayList<String>();
	static String sDate = null;
	static boolean[] free = new boolean[240000];
	static int freeCount = 0;
	static int meetCount = 0;
	static int bestIndex = 0;

        /**
         * Main method for Calendaring assignment. Handles user input and creates new .ics files and compares 
         * sets of .ics files to find free or possible meeting times.
         * 
         * @param args Any command-line arguments. 
         * 
         */ 

	public static void main(String[] args) throws IOException {
		do {
			System.out.println("Please enter the number of your choice:");
			System.out.println("1) Create/Modify Calendar");
			System.out.println("2) Create Free Time Calendar");
			System.out.println("3) Create Meeting Time Calendar");
			System.out.println("0) Quit");
			start = getChar();
			switch (start) {
			case '1':
				badInput = true;
				System.out.println("1) Create New Calendar");
				System.out.println("2) Use Existing Calendar");
				System.out.println("0) Quit");
				do {
					choice = getChar();
					switch (choice) {

					// Create New Calendar
					case '1':
						badInput = false;
						System.out.println("Name the calender");
						String name = getLine();
						System.out.println("Created " + name + ".ics file.");
						writer = new FileWriter(name + ".ics");
						newCalendar = true;
						break;

						// Use Existing Calendar
					case '2':
						badInput = false;
						do {
							System.out.println("Please enter the file path of an existing calendar file:");
							filepath = getLine();
							file = new File(filepath);
							if (file.exists()) {
								break;
							} else
								System.out.println("Sorry, the path you entered is invalid.");
						} while (!file.exists());

						// Removes the END:VCALENDAR
						// Source Referenced:
						// http://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it
						File temp = new File("temp.ics");
						BufferedReader bReader = new BufferedReader(new FileReader(file));
						BufferedWriter bWriter = new BufferedWriter(new FileWriter(temp));
						String line;
						while ((line = bReader.readLine()) != null) {
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

					default:
						System.out.println("Bad Input: " + choice);
						break;
					}
				} while (badInput);

				// Version (section 3.7.4 of RFC 5545)
				// EX: VERSION:2.0

				// Classification (3.8.1.3). Note this is a way of users
				// designating
				// events as public (default), private, or confidential.
				// classvalue = "PUBLIC" / "PRIVATE" / "CONFIDENTIAL"
				// EX: CLASS:PUBLIC
				badInput = true;
				do {
					System.out
					.println("Please enter a number corresponding to how you'd like to classify your event:");
					System.out.println("1) Public");
					System.out.println("2) Private");
					System.out.println("3) Confidential");
					choice = getChar();
					switch (choice) {
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

					default:
						System.out.println("Bad Input");
						break;
					}

				} while (badInput);

				// Location (3.8.1.7)
				// EX: LOCATION:Conference Room - F123\, Bldg. 002
				// EX2:
				// LOCATION;ALTREP="http://xyzcorp.com/conf-rooms/f123.vcf":
				// Conference Room - F123\, Bldg. 002
				System.out.println("Enter Event Location:");
				location = getLine();

				// Priority (3.8.1.9)
				// EX: PRIORITY:1

				badInput = true;
				do {
					System.out
					.println("Please enter a number corresponding to the priority for this event.");
					System.out.println("(First digit will be used)");
					System.out.println("0) No Priority");
					System.out.println("1 - 4) High Priority");
					System.out.println("5) Medium Priority");
					System.out.println("6 - 9) Low Priority");
					choice = getChar();

					if (choice >= '0' && choice <= '9') {
						priority = choice;
						badInput = false;
					} else
						System.out.println("Bad Input: " + choice);
				} while (badInput);

				// Summary (3.8.1.12)
				// EX: SUMMARY:Department Party
				badInput = true;
				do {
					System.out.println("Please enter a summary for this event");
					// System.out.println("Your summary does not have to be a long one.");
					// System.out.println("For example: Department Party.");
					summary = keyboard.nextLine();
					if (summary.isEmpty()) {
						System.out.println("Error: Summary is Empty");
					} else
						badInput = false;
				} while (badInput);

				// Coming back to file writing stuff later

				// DTSTART (3.8.2.4)
				// EX: DTSTART:19980118T073000Z

				badDate = true;
				do {
					possibleDate = "";
					try {
						badInput = true;
						do {
							System.out.println("Please enter the starting month for this event.\nMM");
							startMonth = getLine();
							if (startMonth.length() == 2) {
								badInput = false;
								check = Integer.parseInt(startMonth);
								possibleDate = possibleDate + startMonth;
							}
						} while (badInput);

						badInput = true;
						do {
							System.out.println("Please enter the starting day for this event.\ndd");
							startDay = getLine();
							if (startDay.length() == 2) {
								badInput = false;
								check = Integer.parseInt(startDay);
								possibleDate = possibleDate + "/" + startDay;
							}
						} while (badInput);

						badInput = true;
						do {
							System.out
							.println("Please enter the starting year for this event.\nyyyy");
							startYear = getLine();
							if (startYear.length() == 4) {
								badInput = false;
								check = Integer.parseInt(startYear);
								possibleDate = possibleDate + "/" + startYear;
							}
						} while (badInput);

						if (dateExists(possibleDate)) {
							badDate = false;
						} else
							System.out.println("Invalid Date: " + possibleDate);
					} catch (NumberFormatException e) {
						System.out.println("Error: Not a number!");
					}
				} while (badDate);

				badInput = true;
				do {
					System.out
					.println("Please enter a number corresponding to event duration");
					System.out.println("1) All day");
					System.out.println("2) Duration");

					choice = getChar();
					switch (choice) {
					case '1':
						badInput = false;
						allDay = true;
						break;

					case '2':
						badInput = false;
						allDay = false;
						break;
					default:
						System.out.println("Bad Input");
						break;
					}

				} while (badInput);

				// DTEND (3.8.2.2)
				// EX: DTEND:19960401T150000Z
				// EX2: DTEND;VALUE=DATE:19980704

				badDate = true;
				endMonth = startMonth;
				endYear = startYear;
				if (allDay) {
					int dayEnd = Integer.parseInt(startDay) + 1;
					if (dayEnd < 10) {
						endDay = "0" + dayEnd;
					} else
						endDay = "" + dayEnd;
					;
				} else {
					do {
						possibleDate = "";
						try {
							badInput = true;
							do {
								System.out.println("Please enter the ending month for this event.\nMM");
								endMonth = getLine();
								if (endMonth.length() == 2) {
									check = Integer.parseInt(endMonth);
									if (check < Integer.parseInt(startMonth)) {
										System.out.println("Error: Ending before starting");
									} else {
										badInput = false;
										possibleDate = possibleDate + endMonth;
									}
									
								}
							} while (badInput);

							badInput = true;
							do {
								System.out.println("Please enter the ending day for this event.\ndd");
								endDay = getLine();
								if (endDay.length() == 2) {
									check = Integer.parseInt(endDay);
									if(check < Integer.parseInt(startDay) && Integer.parseInt(startMonth) == Integer.parseInt(endMonth)) {
										System.out.println("Error: Ending before starting");
									} else badInput = false;
									possibleDate = possibleDate + "/" + endDay;
								}
							} while (badInput);

							possibleDate = possibleDate + "/" + endYear;
							if (dateExists(possibleDate)) {
								badDate = false;
							} else
								System.out.println("Invalid Date: " + possibleDate);
						} catch (NumberFormatException e) {
							System.out.println("Error: Not a number!");
						}
					} while (badDate);

					// Get starting time
					badInput = true;
					do {
						timeEnd = null;
						System.out.println("Please enter the starting time [in a 24 hour clock format (hhmmss)] for this event.");
						System.out.println("Example 1:00 PM: 130000");
						try {
							timeStart = getLine();
							check = Integer.parseInt(timeStart);
							if (timeStart.length() == 6) {
								if (timeExists(timeStart)) {
									badInput = false;
								} else
									System.out.println("Error: Invalid Time!");
							}

						} catch (NumberFormatException e) {
							System.out.println("Error: Not a number!");
						}
					} while (badInput);

					// Get ending time
					badInput = true;
					do {
						timeEnd = null;
						System.out.println("Please enter the ending time [in a 24 hour clock format (hhmmss)] for this event.");
						System.out.println("Example 1:30 PM: 133000");
						try {
							timeEnd = getLine();
							check = Integer.parseInt(timeEnd);
							if (timeEnd.length() == 6) {
								if ((Integer.parseInt(timeEnd) > Integer.parseInt(timeStart)) || (Integer.parseInt(endDay) > Integer.parseInt(startDay))) {
									if (timeExists(timeEnd)) {
										badInput = false;
									} else
										System.out.println("Error: Invalid Time!");
								} else
									System.out.println("Error: Ending before starting");
							}

						} catch (NumberFormatException e) {
							System.out.println("Error: Not a number!");
						}
					} while (badInput);
				}

				if (allDay) {
					dateStart = startYear + "" + startMonth + "" + startDay;
					dateEnd = endYear + "" + endMonth + "" + endDay;
				} else {
					dateStart = startYear + "" + startMonth + "" + startDay + "T" + timeStart;
					dateEnd = endYear + "" + endMonth + "" + endDay + "T" + timeEnd;
				}

				// Time zone identifier (3.8.3.1, and whatever other sections
				// you need
				// to be able to specify time zones)
				// EX: TZID:America/New_York
				TimeZone timeZone = TimeZone.getDefault();
				tzid = timeZone.getID();

				if (newCalendar) {
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
				writer.write("TZID:" + tzid + "\n");
				writer.write(endEvent);
				writer.write(endCalendar);
				writer.close();
				System.out.println();
				break;

			case '2':
				/**
				 * The input is a list of .ics filenames (pathnames, if necessary) 
				 * that describe events all on the same date and in the same time zone. 
				 * The output is .ics files describing free times between the inputted events. 
				 * The Summary field of the output .ics files shall be Free Time.
				 */
				String[] files;
				do {
					System.out.println("Enter list of .ics files or pathnames for freeTime.ics creation");
					System.out.println("Ex: event.ics event2.ics");
					System.out.println("(Events must be on same date and same time zone)");
					options = getLine();
					files = options.split("\\s+");
					assert(files.length != 0);
				}
				while(badFiles(files));
				
				readFiles(files);
				assert((times.size() % 2) == 0);
				Arrays.fill(free, true);
				for(int x = 0; x < times.size(); x = x + 2) {
					fillTime(Integer.parseInt(times.get(x)), Integer.parseInt(times.get(x+1)));
				}
				createFree();
				System.out.println("Number of free time calendar files created: " + freeCount);
				
				break;

			case '3':
				String[] mfiles;
				do {
					System.out.println("Enter two list of .ics files or pathnames for meetTime.ics creation");
					System.out.println("Ex: event.ics event2.ics");
					System.out.println("(Events must be on same date and same time zone)");
					System.out.println("\nEnter first list of .ics files:");
					String tmp = getLine();
					System.out.println("\nEnter second list of .ics files:");
					String tmp1 = getLine();
					options = tmp + " " + tmp1;
					System.out.println(options);
					mfiles = options.split("\\s+");
				}
				while(badFiles(mfiles));
				assert(mfiles.length != 0);
				readFiles(mfiles);
				assert((times.size() % 2) == 0);
				Arrays.fill(free, true);
				for(int x = 0; x < times.size(); x = x + 2) {
					fillTime(Integer.parseInt(times.get(x)), Integer.parseInt(times.get(x+1)));
				}
				findBestMeet(mfiles);
				createMeet();
				System.out.println("Number of meet time calendar files created: " + meetCount);
				
				break;

			case '0':
				System.out.println("Closed");
				System.exit(0);
				break;

			default:
				System.out.println("Bad Input: " + choice);
				break;
			}
		} while (isOn);
	}

	// to dos
	
	/**
	 * Method to create a .ics file showing free times based on existing .ics files a user feeds into the program.
	 * Implements Check-in 2. 
	 * 
	 * @param None.
	 * @return None.
	 * @throws IOException For cases like not being able to open a particular .ics file.
	 * 
	 */ 
	
	
	public static void createFree() throws IOException {
		boolean toggle = true;
		freeCount = 0;
			for(int x = 0; x < 240000; x++) {
				if(free[x] == true && toggle == true) {
					freeCount++;
					writer = new FileWriter("freeTime" + freeCount + ".ics");
					writer.write(beginCalendar);
					writer.write(version);
					toggle = false;
					timeStart = pad0(x);
					writer.write(beginEvent);
					writer.write("PRIORITY:0\n");
					writer.write("SUMMARY:Free Time\n");
					writer.write("DTSTART:" + dateStart + "T" + timeStart + "\n");
				}
				else if (free[x] == false && toggle == false) {
					toggle = true;
					timeEnd = pad0(x);
					writer.write("DTEND:" + dateEnd + "T" + timeEnd +"\n");
					writer.write("TZID:" + tzid + "\n");
					writer.write(endEvent);
					writer.write(endCalendar);
					writer.close();
				}
			}
			if(free[239999] == true && toggle == false) {
				writer.write("DTEND:" + dateEnd + "T" + "235959" +"\n");
				writer.write("TZID:" + tzid + "\n");
				writer.write(endEvent);
				writer.write(endCalendar);
			}
			writer.close();
	}
	
	/**
	 * Method to find possible meeting times between two sets of .ics files for two people.
	 * Similar to createFree(). Implements Check-in 3. 
	 * 
	 * @param None
	 * @return None
	 * @throws IOException For cases like not being able to open a particular .ics file.
	 * 
	 */
	
	public static void createMeet() throws IOException {
		boolean toggle = true;
		meetCount = 0;
			for(int x = 0; x < 240000; x++) {
				if(free[x] == true && toggle == true) {
					meetCount++;
					writer = new FileWriter("meetTime" + meetCount + ".ics");
					writer.write(beginCalendar);
					writer.write(version);
					toggle = false;
					timeStart = pad0(x);
					writer.write(beginEvent);
					writer.write("PRIORITY:0\n");
					if((meetCount - 1) == bestIndex) 
						writer.write("SUMMARY:BEST POSSIBLE MEETING TIME\n");
					else writer.write("SUMMARY:POSSIBLE MEETING TIME\n");
					writer.write("DTSTART:" + dateStart + "T" + timeStart + "\n");
				}
				else if (free[x] == false && toggle == false) {
					toggle = true;
					timeEnd = pad0(x);
					writer.write("DTEND:" + dateEnd + "T" + timeEnd +"\n");
					writer.write("TZID:" + tzid + "\n");
					writer.write(endEvent);
					writer.write(endCalendar);
					writer.close();
				}
			}
			if(free[239999] == true && toggle == false) {
				writer.write("DTEND:" + dateEnd + "T" + "235959" +"\n");
				writer.write("TZID:" + tzid + "\n");
				writer.write(endEvent);
				writer.write(endCalendar);
			}
			writer.close();
	}
	
	/**
	 * Method to determine the best meeting time out of possible meeting times between two people.
	 * Implements Check-in 3. 
	 * 
	 * @param String[] files A string array holding paths for a bunch of .ics files to examine
	 * @return None
	 * 
	 */ 
	
	public static void findBestMeet(String[] files) {
		boolean toggle = true;
		ArrayList<Integer> priorities = new ArrayList<Integer>();
		int priority = 0;
		int filePriority = 0;
		String starting = "000000";
		String ending = "000000";
			for(int x = 0; x < 240000; x++) {
				if(free[x] == true && toggle == true) {
					toggle = false;
					starting = pad0(x);
				}
				else if (free[x] == false && toggle == false) {
					toggle = true;
					ending = pad0(x);
					// Search files for priority to fit the free time
					for(int y = 0; y < files.length; y++) {
						priority = 0;
						filePriority = 0;
						String line;
						try{
							filepath = files[y];
							file = new File(filepath);
							BufferedReader bReader = new BufferedReader(new FileReader(file));
							while ((line = bReader.readLine()) != null) {
								if(line.contains("DTSTART") || line.contains("DTEND")) {
									assert(line.length() > 16);
									String[] parts = line.split(":");
									String[] timeParts = parts[1].split("T");
									String time = timeParts[1];
									if(time.equals(starting) || time.equals(ending)) {
										priority = priority + filePriority;
									}
								}
								// No priority is good priority so equation priority will use 10
								else if(line.contains("PRIORITY")) {
									if(line.charAt(9) == ('0')) {
										filePriority = 10;
									}
									else filePriority = (int)line.charAt(9);
								}

							}
							bReader.close();
						}
						catch (Exception e) {
							e.getMessage();
						}
					} // End loop for searching files
					priorities.add(priority);
				}
			}
			int max = 0;
			bestIndex = 0;
			for(int x = 0; x < priorities.size(); x++) {
				if(priorities.get(x) > max)
				{
					max = priorities.get(x);
					bestIndex = x;
				}
			}
	}
	
	/**
	 * Changes a given number to a string time format
	 * @param num
	 * @return 6 length string version of the number
	 */
	public static String pad0(int num) {
		int length = String.valueOf(num).length();
		String padded = "";
		for(int x = 0; x < (6 - length); x++) {
			padded = padded + 0;
		}
		padded = padded + num;
		return padded;
	}
	
	// Fills part of the free boolean array to false given the start and end locations
	public static void fillTime(int start, int end) {
		for(int x = start; x < end; x++) {
			free[x] = false;
		}
	}
	
	/**
	 * Method to read a bunch of .ics files and collect pertinent information from them.
	 * 
	 * @param String[] files String array holding paths for a bunch of .ics files to read from.
	 * @return None
	 * 
	 */ 
	
	public static void readFiles(String[] files) {
		times = new ArrayList<String>();
		// Get information from first file
		String line;
		try{
			filepath = files[0];
			file = new File(filepath);
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			while ((line = bReader.readLine()) != null) {
				if(line.contains("DTSTART")) {
					assert(line.length() > 16);
					String[] parts = line.split(":");
					String[] timeParts = parts[1].split("T");
					sDate = timeParts[0];
					String time = timeParts[1];
					assert(time.length() == 6);
					times.add(time);
				}
				else if(line.contains("DTEND")) {
					getTime(line);
				}
				else if(line.contains("TZID")) {
					tzid = line;
				}
			}
			assert(sDate != null);
			dateStart = sDate;
			dateEnd = sDate;
			bReader.close();
			
			// Read rest of the files
			for(int x = 1; x < files.length; x++) {
				filepath = files[x];
				file = new File(filepath);
				bReader = new BufferedReader(new FileReader(file));
				
				while ((line = bReader.readLine()) != null) {
					if(line.contains("DTSTART") || line.contains("DTEND")) {
						assert(line.length() > 16);
						getTime(line);
					}
					else if(line.contains("TZID")) {
						assert(line.equals(tzid));
					}
				}
				bReader.close();
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
		
	}
	
	/** 
	 * Method that checks if input .ics files are valid files.
	 * 
	 * @param String[] A string array containing paths for a bunch of .ics files to examine.
	 * @return boolean True if bad file path found, false otherwise.
	 * 
	 */
	 
	public static boolean badFiles(String[] files) {
		for(int x = 0; x < files.length; x++) {
			filepath = files[x];
			file = new File(filepath);
			if (!file.exists()) {
				System.out.println("Error: Check filename or filepath.");
				return true;
			}	
		}
		return false;
	}
	
	/**
	 * Method that gets the time from the dtstart/dtend and adds to the ArrayList times.
	 * 
	 * @param String line The dtstart/dtend string to obtain time from.
	 * @return None
	 * 
	 */
	 
	public static void getTime(String line) {
		String[] parts = line.split(":");
		String[] timeParts = parts[1].split("T");
		String day = timeParts[0];
		assert(sDate.equals(day));
		String time = timeParts[1];
		times.add(time);
	}
	
	/** 
	 * Method that returns first char from keyboard buffer. 
	 * 
	 * @param None
	 * @return char options.charAt(0) The character at position 0 of the keyboard buffer.
	 * 
	 */
	 
	public static char getChar() {
		empty = true;
		do {
			options = keyboard.nextLine();
			if (!options.isEmpty())
				empty = false;
		} while (empty);
		return (options.charAt(0));
	}

	/**
	 * Method that returns the next line from keyboard buffer.
	 * 
	 * @param None
	 * @return String options The next line in the keyboard buffer.
	 * 
	 */
	 
	public static String getLine() {
		empty = true;
		do {
			options = keyboard.nextLine();
			if (!options.isEmpty())
				empty = false;
		} while (empty);
		return (options);
	}

	/**
	 * Method that checks if a given date exists. 
	 * 
	 * Reference:
	 * http://stackoverflow.com/questions/4516572/checking-if-a-date-exists-or-not
	 * 
	 * @author user467871.
	 * 
	 * @param String date The date to check for.
	 * @return boolean true if given date exists, false otherwise. 
	 * 
	 */
	public static boolean dateExists(String date) {
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


        /**
         * Method that checks if a given time exists. Similar to dateExists(String date).
         * 
         * @param String time The time to check for. 
         * @return boolean true if given time exists, false otherwise.
         * 
         */
         
	public static boolean timeExists(String time) {
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
