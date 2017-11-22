import java.util.*;

public class EventComparator implements Comparator<Event>{
	@Override
	public int compare(Event e1, Event e2) {
		if (e1.getEventStartingTime().substring(0, 2).equals(e2.getEventStartingTime().substring(0, 2)))
			return Integer.parseInt(e1.getEventStartingTime().substring(3, 5)) - Integer.parseInt(e2.getEventStartingTime().substring(3, 5));
		return Integer.parseInt(e1.getEventStartingTime().substring(0, 2)) - Integer.parseInt(e2.getEventStartingTime().substring(0, 2));
	}
}
