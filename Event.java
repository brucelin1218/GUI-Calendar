/**
 * CS151
 * Homework Assignment #4: GUI Calendar
 * Instructor: Dr. Kim 
 * @author BruceLin
 * Due Date: 11/20/2017
 */

import java.io.Serializable;

public class Event implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// The event constructor
	/**
	 * Event Constructor
	 * @param eventTitle
	 * @param eventDate
	 * @param eventStartingTime
	 * @param eventEndingTime
	 */
	public Event(String eventTitle, String eventDate, String eventStartingTime, String eventEndingTime) {
		this.eventTitle = eventTitle;
		this.eventDate = eventDate;
		this.eventStartingTime = eventStartingTime;
		this.eventEndingTime = eventEndingTime;
		this.isEnd = false;
	}
	
	// The accessors 
	/**
	 * Get the event title.
	 * @return the event title.
	 */
	public String getEventTitle() { return this.eventTitle; }
	
	/**
	 * Get the event date.
	 * @return the event date.
	 */
	public String getEventDate() { return this.eventDate; }
	
	/**
	 * Get the event starting time.
	 * @return the event starting time.
	 */
	public String getEventStartingTime() { return this.eventStartingTime; }
	
	/**
	 * Get the event ending time.
	 * @return the event ending time.
	 */
	public String getEventEndingTime() { return this.eventEndingTime; }
	
	/**
	 * Get if the event ends or not.
	 * @return the boolean if the event ends or not. True if ends; false otherwise.
	 */
	public boolean IsEnd() { return this.isEnd; }
	
	/**
	 * Display the event info
	 */
	public String display() {
		if (this.eventEndingTime.equals(""))
			return this.eventStartingTime + ": " + this.eventTitle;
		else
			return this.eventStartingTime + " - " + this.eventEndingTime + ": " + this.eventTitle;	
	}
	
	/** 
	 * @Override the method compare
	 * @param e1 Event 1
	 * @param e2 Event 2
	 * @return 1 if the starting time of event 1 is greater, -1 if smaller, 0 otherwise.
	 */
	public int compare (Event e1, Event e2) {
		return e1.getEventStartingTime().compareTo(e2.getEventStartingTime());
	}
	
	// Instance Variables
	private String eventTitle;
	private String eventDate;
	private String eventStartingTime;
	private String eventEndingTime;
	private boolean isEnd;
}
