import java.io.IOException;

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
