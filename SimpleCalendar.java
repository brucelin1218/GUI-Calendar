import java.io.IOException;

/**
 * CS151
 * Homework Assignment #4: GUI Calendar
 * Instructor: Dr. Kim 
 * @author BruceLin
 * Due Date: 11/20/2017
 */

public class SimpleCalendar {
	/**
	 * The driver for the GUI Calendar.
	 * @param args not used.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		CalendarModel model = new CalendarModel();
		CalendarView view = new CalendarView(model);
		model.attach(view);
	}
}
