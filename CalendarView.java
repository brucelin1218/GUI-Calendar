import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarView implements ChangeListener {
	/**
	 * Constructs the calendar.
	 * @param model the  model that stores and manipulates calendar data
	 */
	public CalendarView(CalendarModel model) {
		this.model = model;
		this.maxDays = model.getMaxDays();
		this.monthView.setLayout(new GridLayout(0, 7));
		this.dayTextPane.setPreferredSize(new Dimension(500, 250));
		this.dayTextPane.setEditable(false);
		this.createDayBtns();
		this.addBlankBtns();
		this.addDayBtns();
		this.highlightEvents();
		this.showDate(model.getSelectedDay());
		this.highlightSelectedDate(model.getSelectedDay() - 1);
		this.createBtn.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					createEventDialog();
				}
			}
		);
		
		JButton prevMonth = new JButton("<");
		prevMonth.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					model.prevMonth();
					createBtn.setEnabled(false);
					nextDayBtn.setEnabled(false);
					prevDayBtn.setEnabled(false);
					dayTextPane.setText("");
				}
			}
		);
		
		JButton nextMonth = new JButton(">");
		nextMonth.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					model.nextMonth();
					createBtn.setEnabled(false);
					nextDayBtn.setEnabled(false);
					prevDayBtn.setEnabled(false);
					dayTextPane.setText("");
				}
			}
		);
		
		// Create a month container to contain the month view
		JPanel monthContainer = new JPanel();
		monthContainer.setLayout(new BorderLayout());
		monthLabel.setText(arrayOfMonths[model.getCurrentMonth()] + " " + model.getCurrentYear());
		monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Create a header panel for "<" "current month" ">"
		JPanel header = new JPanel();
		header.setLayout(new BorderLayout());
		header.add(prevMonth, BorderLayout.LINE_START);
		header.add(monthLabel, BorderLayout.CENTER);
		header.add(nextMonth, BorderLayout.LINE_END);
		
		// The month container
		monthContainer.add(header, BorderLayout.PAGE_START);
		monthContainer.add(new JLabel("        S               M               T               W               T               F               S        ", JLabel.CENTER), BorderLayout.CENTER);
		monthContainer.add(monthView, BorderLayout.PAGE_END);
		
		// Create the single day view 
		JPanel dayViewPanel = new JPanel();
		dayViewPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		
		// Add the vertical scroll bar to the text pane
		JScrollPane dayScrollPane = new JScrollPane(dayTextPane);
		dayScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		dayViewPanel.add(dayScrollPane, c);
		JPanel btnsPanel = new JPanel();
		
		// Controller for next day button
		nextDayBtn.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					model.nextDay();
				}
			}
		);
		
		// Controller for previous day button
		prevDayBtn.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					model.prevDay();
				}
			}
		);
		
		btnsPanel.add(prevDayBtn);
		btnsPanel.add(createBtn);
		btnsPanel.add(nextDayBtn);
		c.gridx = 0;
		c.gridy = 1;
		dayViewPanel.add(btnsPanel, c);

		JButton quit = new JButton("Quit Program");
		
		// Controller for quit button
		quit.addActionListener(
			new ActionListener() {
			@Override
				public void actionPerformed(ActionEvent e) {
					model.saveEvents();
					System.exit(0);
				}
			}
		);
		
		btnsPanel.add(quit);
		frame.add(monthContainer);
		frame.add(dayViewPanel);
		
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (model.isMonthChanged()) {
			maxDays = model.getMaxDays();
			dayBtns.clear();
			monthView.removeAll();
			monthLabel.setText(arrayOfMonths[model.getCurrentMonth()] + " " + model.getCurrentYear());
			createDayBtns();
			addBlankBtns();
			addDayBtns();
			highlightEvents();
			prevHighlight = -1;
			model.resetMonthChanged();
			frame.pack();
			frame.repaint();
		} else {
			showDate(model.getSelectedDay());
			highlightSelectedDate(model.getSelectedDay() - 1);
		}
	}
	
	/**
	 * Shows the selected date and events on that date
	 * @param d
	 */
	private void showDate(final int d) {
		model.setSelectedDay(d);
		String dayOfWeek = arrayOfDays[model.getDayOfWeek(d) - 1] + "";
		String date = (model.getCurrentMonth() + 1) + "/" + d + "/" + model.getCurrentYear();
		String str = "";
		
		if (model.hasEvent(date)) {
			str += model.getEvents(date) + "\n";
		}
	
		dayTextPane.setText(dayOfWeek + " " + date + "\n" + str);
		dayTextPane.setCaretPosition(0);
	}
	
	/**
	 * Highlights the currently selected date.
	 * @param d the currently selected date
	 */
	private void highlightSelectedDate(int d) {
		Border border = new LineBorder(Color.RED, 2);
		dayBtns.get(d).setBorder(border);
		
		if (prevHighlight != -1) {
			dayBtns.get(prevHighlight).setBorder(new JButton().getBorder());
		}
		
		prevHighlight = d;
	}
	
	/**
	 * Highlights the days contained events
	 */
	private void highlightEvents() {
		for (int i = 1; i <= maxDays; i++) {
			if (model.hasEvent((model.getCurrentMonth() + 1) + "/" + i + "/" + model.getCurrentYear())) {
				dayBtns.get(i - 1).setBackground(Color.pink);
				dayBtns.get(i -1).setOpaque(true);
			}
		}
	}
	
	/**
	 * Adds the buttons representing the days of the month to the panel.
	 */
	private void addDayBtns() {
		for (JButton d : dayBtns) {
			monthView.add(d);
			d.setPreferredSize(new Dimension(40, 50));
		}
	}
	
	/**
	 * Adds the blank buttons as the fillers.
	 */
	private void addBlankBtns() {
		for (int j = 1; j < model.getDayOfWeek(1); j++) {
			JButton blank = new JButton();
			blank.setEnabled(false);
			blank.setBorder(null);
			monthView.add(blank);
		}
	}
	
	/**
	 * Adds the days as buttons.
	 */
	private void createDayBtns() {
		for (int i = 1; i <= maxDays; i++) {
			final int d = i;
			JButton day = new JButton(Integer.toString(d));
			day.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showDate(d);
						highlightSelectedDate(d - 1);
						createBtn.setEnabled(true);
						nextDayBtn.setEnabled(true);
						prevDayBtn.setEnabled(true);
					}
				}
			);
			dayBtns.add(day);
		}
	}
	
	/**
	 * Create the event creating dialog
	 */
	private void createEventDialog() {
		final JDialog eventDialog = new JDialog();
		eventDialog.setTitle("Create Event");
		eventDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		final JTextField eventText = new JTextField(40);
		final JTextField startTime = new JTextField(12);
		final JTextField endTime = new JTextField(12);
		JButton createEvent = new JButton("Save");
		
		createEvent.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (eventText.getText().isEmpty())
						return;
					
					if (!eventText.getText().isEmpty() && (startTime.getText().isEmpty() || 
							endTime.getText().isEmpty()) || startTime.getText().length() != 5
							|| endTime.getText().length() != 5
							|| !startTime.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")
							|| !endTime.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
						JDialog timeErrorMessage = new JDialog();
						timeErrorMessage.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
						timeErrorMessage.setLayout(new GridLayout(2, 0));
						timeErrorMessage.add(new JLabel("Please enter start and end time in format XX:XX."));
						
						JButton continueButton = new JButton("Continue");
						
						continueButton.addActionListener(
							new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									timeErrorMessage.dispose();
								}
							}
						);
						
						timeErrorMessage.add(continueButton);
						timeErrorMessage.pack();
						timeErrorMessage.setVisible(true);
					} else if (!eventText.getText().equals("")) {
						if (model.eventConflict(startTime.getText(), endTime.getText())) {
								JDialog conflictDialog = new JDialog();
								conflictDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
								conflictDialog.setLayout(new GridLayout(3, 10));
								conflictDialog.add(new JLabel("     Error: Time conflict     "));
								JButton continueBtn = new JButton("  Continue  ");
								
								continueBtn.addActionListener(
									new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											conflictDialog.dispose();
										}
									}
								);
								
								conflictDialog.add(continueBtn);
								conflictDialog.pack();
								conflictDialog.setVisible(true);
						} else {
							eventDialog.dispose();
							model.createEvent(eventText.getText(), startTime.getText(), endTime.getText());
							showDate(model.getSelectedDay());
							highlightEvents();
						}
					}
				}
			}
		);
		
		eventDialog.setLayout(new GridBagLayout());
		JLabel date = new JLabel();
		date.setText(model.getCurrentMonth() + 1 + "/" + model.getSelectedDay() + "/" + model.getCurrentYear());
		date.setBorder(BorderFactory.createEmptyBorder());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);
		c.gridx = 0;
		c.gridy = 0;
		eventDialog.add(date, c);
		c.gridy = 1;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.LINE_START;
		eventDialog.add(new JLabel("Event Title"), c);
		c.gridy = 2;
		eventDialog.add(eventText, c);
		c.gridy = 3;
		c.weightx = 0.0;
		c.anchor = GridBagConstraints.LINE_START;
		eventDialog.add(new JLabel("Event Start Time (XX:XX)"), c);
		c.anchor = GridBagConstraints.CENTER;
		eventDialog.add(new JLabel("Event End Time (XX:XX)"), c);
		c.gridy = 4;
		c.anchor = GridBagConstraints.LINE_START;
		eventDialog.add(startTime, c);
		c.anchor = GridBagConstraints.CENTER;
		eventDialog.add(endTime, c);
		c.anchor = GridBagConstraints.LINE_END;
		eventDialog.add(createEvent, c);
		eventDialog.pack();
		eventDialog.setVisible(true);
	}
	
	// Instance Variables
	private CalendarModel model;
	private DAYS[] arrayOfDays = DAYS.values();
	private MONTHS[] arrayOfMonths = MONTHS.values();
	private int prevHighlight = -1;
	private int maxDays;
	
	private JFrame frame = new JFrame("My GUI Calendar");
	private JPanel monthView = new JPanel();
	private JLabel monthLabel = new JLabel();
	private JButton createBtn = new JButton("Create Event");
	private JButton nextDayBtn = new JButton("Next Day");
	private JButton prevDayBtn = new JButton("Prev Day");
	private JTextPane dayTextPane = new JTextPane();
	private ArrayList<JButton> dayBtns = new ArrayList<JButton>();
}
