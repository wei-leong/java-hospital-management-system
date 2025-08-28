package Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewFeedback extends JPanel {

	// Member variables for UI components
	private JTable feedbackTable;
	private DefaultTableModel tableModel;
	private JPanel filterPanel;
	private JPanel starPanel;
	// Using JCheckBoxes with a ButtonGroup for mutually exclusive selection
	private JCheckBox todayFilter, thisWeekFilter, thisMonthFilter, thisYearFilter, showAllFilter;
	private JButton clearButton;
	private ButtonGroup filterGroup;

	// Doctor information passed from login
	private String doctorId;

	// Store all feedback for the doctor in memory
	private List<String[]> allDoctorFeedbacks = new ArrayList<>();

	// Paths to image and text files
	private static final String FEEDBACK_FILE_PATH = "src/txt/feedback.txt";
	private static final String STAR_IMAGE_PATH = "src/image/star.png";
	private static final String STAR_GLOW_IMAGE_PATH = "src/image/star-glow.png";
	private static final int RATING_COUNT = 5; // Total number of stars to display

	/**
	 * Main constructor for the ViewFeedback class. Initializes the UI and loads
	 * the feedback data.
	 *
	 * @param userInfo An array containing the logged-in user's information.
	 * Assumes the doctor ID is at index 0.
	 */
	public ViewFeedback(String[] userInfo) {
		// Retrieve the doctor ID from the user info array
		if (userInfo != null && userInfo.length > 0) {
			this.doctorId = userInfo[0];
		} else {
			// Handle case where no user info is passed
			JOptionPane.showMessageDialog(this, "User information not provided. Cannot display feedbacks.",
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Use BorderLayout for the main content pane
		this.setLayout(new BorderLayout());

		// --- Component Initialization ---
		// Filter Checkboxes and Button Panel
		filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Feedbacks by Date"));
		todayFilter = new JCheckBox("Today");
		thisWeekFilter = new JCheckBox("This Week");
		thisMonthFilter = new JCheckBox("This Month");
		thisYearFilter = new JCheckBox("This Year");
		showAllFilter = new JCheckBox("Show All");
		clearButton = new JButton("Clear Filter");

		// Group the checkboxes to make them mutually exclusive
		filterGroup = new ButtonGroup();
		filterGroup.add(todayFilter);
		filterGroup.add(thisWeekFilter);
		filterGroup.add(thisMonthFilter);
		filterGroup.add(thisYearFilter);
		filterGroup.add(showAllFilter);

		// Add check boxes and button to the filter panel
		filterPanel.add(new JLabel("Filter by Date:"));
		filterPanel.add(todayFilter);
		filterPanel.add(thisWeekFilter);
		filterPanel.add(thisMonthFilter);
		filterPanel.add(thisYearFilter);
		filterPanel.add(showAllFilter);
		filterPanel.add(clearButton);

		// Star Rating Panel
		starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
		starPanel.setBackground(new Color(240, 240, 240));

		// Combine filter and star panels into a single top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(filterPanel, BorderLayout.NORTH);
		topPanel.add(starPanel, BorderLayout.CENTER);

		// Table Setup
		String[] columnNames = {"Feedback ID", "Customer ID", "Rating", "Message", "Doctor ID", "Date"};
		// Override isCellEditable to prevent any cell from being edited
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		feedbackTable = new JTable(tableModel);
		feedbackTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(feedbackTable);

		// Add the scroll pane and the top panel to the main frame using BorderLayout
		this.add(topPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);

		// --- Event Listeners for Filters ---
		todayFilter.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				updateTableAndStars(filterFeedbacks("today"));
			}
		});
		thisWeekFilter.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				updateTableAndStars(filterFeedbacks("week"));
			}
		});
		thisMonthFilter.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				updateTableAndStars(filterFeedbacks("month"));
			}
		});
		thisYearFilter.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				updateTableAndStars(filterFeedbacks("year"));
			}
		});
		showAllFilter.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				updateTableAndStars(allDoctorFeedbacks);
			}
		});

		clearButton.addActionListener(e -> {
			filterGroup.clearSelection();
			updateTableAndStars(allDoctorFeedbacks);
		});

		// --- Load Data and Initial Display ---
		loadAllFeedbackForDoctor();
		if (allDoctorFeedbacks.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No feedback records found for this doctor.",
				"No Data Found", JOptionPane.INFORMATION_MESSAGE);
			// Even if empty, ensure UI is still initialized with empty table/stars
			updateTableAndStars(new ArrayList<>());
		} else {
			// Initially show all data
			updateTableAndStars(allDoctorFeedbacks);
			showAllFilter.setSelected(true); // Default to show all
		}
	}

	/**
	 * Reads the feedback file, filters by the doctor's ID, and stores the
	 * results.
	 */
	private void loadAllFeedbackForDoctor() {
		allDoctorFeedbacks.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(FEEDBACK_FILE_PATH))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Split the line by comma, and remove leading/trailing spaces
				String[] parts = line.split("\\s*,\\s*");
				if (parts.length >= 6) {
					String staffOrDoctorId = parts[4];
					if (staffOrDoctorId.equals(this.doctorId)) {
						allDoctorFeedbacks.add(parts);
					}
				}
			}
		} catch (IOException e) {
			// Handle file not found or read error
			JOptionPane.showMessageDialog(this, "Error reading feedback file: " + e.getMessage(),
				"File Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Filters the feedback data based on the given time period.
	 *
	 * @param filterType The type of filter to apply ("today", "week", "month",
	 * "year").
	 * @return A list of feedback records that match the filter criteria.
	 */
	private List<String[]> filterFeedbacks(String filterType) {
		List<String[]> filteredList = new ArrayList<>();
		LocalDate now = LocalDate.now();

		for (String[] feedback : allDoctorFeedbacks) {
			try {
				// The date is at index 5 of the feedback array
				LocalDate feedbackDate = LocalDate.parse(feedback[5]);
				boolean matches = false;

				switch (filterType) {
					case "today":
						matches = feedbackDate.isEqual(now);
						break;
					case "week":
						// Get the week of the year based on the system's locale
						WeekFields weekFields = WeekFields.of(Locale.getDefault());
						matches = feedbackDate.get(weekFields.weekOfWeekBasedYear()) == now.get(weekFields.weekOfWeekBasedYear())
							&& feedbackDate.getYear() == now.getYear();
						break;
					case "month":
						matches = feedbackDate.getMonth().equals(now.getMonth()) && feedbackDate.getYear() == now.getYear();
						break;
					case "year":
						matches = feedbackDate.getYear() == now.getYear();
						break;
				}

				if (matches) {
					filteredList.add(feedback);
				}
			} catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
				// Skip records with invalid date format
				System.err.println("Skipping feedback record due to invalid date format: " + feedback[5]);
			}
		}
		return filteredList;
	}

	/**
	 * Calculates the average rating from a list of feedback records.
	 *
	 * @param feedbacks The list of feedbacks to calculate the average from.
	 * @return The calculated average rating. Returns 0 if the list is empty or
	 * no valid ratings are found.
	 */
	private double calculateAverageRating(List<String[]> feedbacks) {
		if (feedbacks.isEmpty()) {
			return 0.0;
		}

		int totalRating = 0;
		int validRatingsCount = 0;

		for (String[] feedback : feedbacks) {
			try {
				// The rating is at index 2 of the feedback array
				String ratingStr = feedback[2];
				// Handle "null" or empty string ratings
				if (ratingStr != null && !ratingStr.equalsIgnoreCase("null") && !ratingStr.trim().isEmpty()) {
					int rating = Integer.parseInt(ratingStr);
					totalRating += rating;
					validRatingsCount++;
				}
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				// Skip records with invalid rating format
				System.err.println("Skipping feedback record due to invalid rating: " + feedback[2]);
			}
		}

		return validRatingsCount > 0 ? totalRating / validRatingsCount : 0.0;
	}

	/**
	 * Updates the star rating display panel based on a given rating.
	 *
	 * @param rating The average rating to be displayed.
	 */
	private void updateStarPanel(double rating) {
		starPanel.removeAll();
		starPanel.add(new JLabel("Average Rating: "));

		// Load star icons
		ImageIcon starIcon = new ImageIcon(STAR_IMAGE_PATH);
		ImageIcon glowStarIcon = new ImageIcon(STAR_GLOW_IMAGE_PATH);

		// Scale images if necessary, though the prompt implies they are pre-sized
		Image starImage = starIcon.getImage();
		Image newStarImage = starImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		starIcon = new ImageIcon(newStarImage);

		Image glowStarImage = glowStarIcon.getImage();
		Image newGlowStarImage = glowStarImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		glowStarIcon = new ImageIcon(newGlowStarImage);

		int glowStars = (int) Math.round(rating);

		for (int i = 0; i < RATING_COUNT; i++) {
			JLabel starLabel = new JLabel();
			if (i < glowStars) {
				starLabel.setIcon(glowStarIcon);
			} else {
				starLabel.setIcon(starIcon);
			}
			starPanel.add(starLabel);
		}

		starPanel.revalidate();
		starPanel.repaint();
	}

	/**
	 * Updates both the table and the star panel with new data.
	 *
	 * @param feedbacks The list of feedbacks to display and use for
	 * calculation.
	 */
	private void updateTableAndStars(List<String[]> feedbacks) {
		updateTable(feedbacks);
		double avgRating = calculateAverageRating(feedbacks);
		updateStarPanel(avgRating);
	}

	/**
	 * Updates the JTable's model with a new set of data.
	 *
	 * @param feedbacks The list of feedbacks to populate the table with.
	 */
	private void updateTable(List<String[]> feedbacks) {
		tableModel.setRowCount(0); // Clear existing table data
		if (feedbacks.isEmpty()) {
			tableModel.addRow(new Object[]{"No feedbacks found for this filter.", "", "", "", "", ""});
		} else {
			for (String[] feedback : feedbacks) {
				tableModel.addRow(feedback);
			}
		}
	}
}
