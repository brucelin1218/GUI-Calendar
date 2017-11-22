/**
 * CS151
 * Homework Assignment #4: GUI Calendar
 * Instructor: Dr. Kim 
 * @author BruceLin
 * Due Date: 11/20/2017
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

enum DAYS { Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday; }

enum MONTHS { January, February, March, April, May, June, July, August, September, October, November, December; }

public class CalendarModel {
	/**
	 * Constructor
	 * @throws IOException 
	 */
	public CalendarModel() throws IOException {
		this.cal = new GregorianCalendar();
		this.listeners = new ArrayList<ChangeListener>();
		this.selectedDay = cal.get(Calendar.DATE);
		this.monthChanged = false;
		this.eventMap = new HashMap<>();
		this.maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		this.loadEvents();
	}
	
	/**
	 * Adds a change listener to the calendar.
	 * @param listener the change listener to add
	 */
	public void attach(ChangeListener listener) { listeners.add(listener); }
	
	/**
	 * Updates all ChangeListeners in calendar
	 */
	public void update() {
		for (ChangeListener l: listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	/**
	 * Sets the user selected day.
	 * @param day the day of the month
	 */
	public void setSelectedDay(int day) { this.selectedDay = day; }
	
	/**
	 * Gets the selected day.
	 * @return the value of the selected day.
	 */
	public int getSelectedDay() { return this.selectedDay; }
	
	/**
	 * Gets the current year.
	 * @return the value of current year.
	 */
	public int getCurrentYear() { return cal.get(Calendar.YEAR); }
	
	/**
	 * Gets the current month.
	 * @return the value of the current month.
	 */
	public int getCurrentMonth() { return cal.get(Calendar.MONTH); }
	
	/**
	 * Gets the day of a week by the value.
	 * @param i
	 * @return the value of 0 - 6 in the week.
	 */
	public int getDayOfWeek(int i) {
		cal.set(Calendar.DAY_OF_MONTH, i);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Gets the maximum days for the month.
	 * @return the value of the maximum days.
	 */
	public int getMaxDays() { return this.maxDays; }
	
	/**
	 * Goes to the the next month.
	 */
	public void nextMonth() {
		cal.add(Calendar.MONTH, 1);
		this.maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		this.monthChanged = true;
		this.update();
	}
	
	/**
	 * Goes to the previous month.
	 */
	public void prevMonth() {
		cal.add(Calendar.MONTH, -1);
		this.maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		this.monthChanged = true;
		this.update();
	}
	
	/**
	 * Goes to the next day.
	 */
	public void nextDay() {
		this.selectedDay++;
		
		if (this.selectedDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			this.nextMonth();
			this.selectedDay = 1;
		}
		this.update();
	}
	
	/**
	 * Goes to the previous day.
	 */
	public void prevDay() {
		this.selectedDay--;
		
		if (this.selectedDay < 1) {
			this.prevMonth();
			this.selectedDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		this.update();
	}
	
	/**
	 * Check to know if the current month changes.
	 * @return true if current month changes, false otherwise.
	 */
	public boolean isMonthChanged() { return this.monthChanged; }
	
	/**
	 * Reset the boolean value of monthChanged to false.
	 */
	public void resetMonthChanged() { this.monthChanged = false; }
	
	/**
	 * Create an event on a specific scheduled date.
	 * @param eventTitle
	 * @param eventStartingTime
	 * @param eventEndingTime
	 */
	public void createEvent(String eventTitle, String eventStartingTime, String eventEndingTime) {
		String eventDate = (cal.get(Calendar.MONTH) + 1) + "/" + selectedDay + "/" + cal.get(Calendar.YEAR);
		Event e = new Event(eventTitle, eventDate, eventStartingTime, eventEndingTime);
		ArrayList<Event> events = new ArrayList<>();
		
		if (hasEvent(e.getEventDate())) {
			events = this.eventMap.get(eventDate);
		}
		
		events.add(e);
		this.eventMap.put(eventDate, events);
	}
	
	/**
	 * Checks if specified date has any events scheduled.
	 * @param date the selected date in format MM/DD/YYYY
	 * @return if the date has an event
	 */
	public Boolean hasEvent(String date) { return eventMap.containsKey(date); }
	
	/**
	 * Gets all the scheduled events from the particular date.
	 * @param eventDate
	 * @return the scheduled events
	 */
	public String getEvents(String eventDate) {
		ArrayList<Event> events = this.eventMap.get(eventDate);
		Collections.sort(events, new EventComparator());
		
		String str = "";
		for (Event e : events) {
			str += e.display() + "\n";
		}
		return str;
	}
	
	/**
	 * Saves all events to "events.ser".
	 */
	public void saveEvents() {
		if (eventMap.isEmpty())
			return;
		
		try {
			FileOutputStream outputfile = new FileOutputStream("events.ser");
			ObjectOutputStream outputObject = new ObjectOutputStream(outputfile);
			outputObject.writeObject(eventMap);
			outputObject.close();
			outputfile.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Check if the scheduled events are conflicted
	 * @param eventStartingTime
	 * @param eventEndingTime
	 * @return
	 */
	public boolean eventConflict(String eventStartingTime, String eventEndingTime) {
		String date = (getCurrentMonth() + 1) + "/" + this.selectedDay + "/" + getCurrentYear();
		
		if (!hasEvent(date)) {
			return false;
		}
		
		ArrayList<Event> events = eventMap.get(date);
		Collections.sort(events, new EventComparator());
		
		int NewEventStartTime = timeConverter(eventStartingTime);
		int NewEventEndTime = timeConverter(eventEndingTime);
		
		for (Event e: events) {
			int eventStartTime = timeConverter(e.getEventStartingTime());
			int eventEndTime = timeConverter(e.getEventEndingTime());
			
			if (NewEventStartTime >= eventStartTime && NewEventStartTime < eventEndTime)
				return true;
			else if (NewEventStartTime < eventStartTime && NewEventEndTime > eventStartTime)
				return true;
		}
		return false;
	}
	
	/**
	 * Convert the hours to minutes 
	 * @param time
	 * @return minutes for the event duration time
	 */
	public int timeConverter(String time) {
		int hours = Integer.valueOf(time.substring(0, 2));
		return hours * 60 + Integer.valueOf(time.substring(3));
	}
	
	/**
	 * Load events from the file "events.ser"
	 */
	public void loadEvents() throws IOException{
		try {
			FileInputStream finput = new FileInputStream("events.ser");
			ObjectInputStream foutput = new ObjectInputStream(finput);
			@SuppressWarnings("unchecked")
			HashMap<String, ArrayList<Event>> temp = (HashMap<String, ArrayList<Event>>) foutput.readObject();
			for (String date: temp.keySet()) {
				if (hasEvent(date)) {
					ArrayList<Event> events = eventMap.get(date);
					events.addAll(temp.get(date));
				} else
					eventMap.put(date, temp.get(date));
			}
			foutput.close();
			finput.close();
		} catch (IOException ioe) {
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found.");
			e.printStackTrace();
		}
	}
	
	// Instance Variables
	private GregorianCalendar cal;
	private ArrayList<ChangeListener> listeners;
	private int selectedDay;
	private boolean monthChanged;
	private HashMap<String, ArrayList<Event>> eventMap;
	private int maxDays;
}
