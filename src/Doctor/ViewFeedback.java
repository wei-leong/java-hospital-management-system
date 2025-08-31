package Doctor;

import Class.FileActions;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewFeedback extends JPanel {

	// GUI component
	private JTable feedbackTable;
	private DefaultTableModel tableModel;
	private JPanel filterPanel;
	private JPanel starPanel;
	
	// use JCheckBoxes with ButtonGroup for mutually exclusive selection
	private JCheckBox todayFilter, thisWeekFilter, thisMonthFilter, thisYearFilter, showAllFilter;
	private JButton clearButton;
	private ButtonGroup filterGroup;

	// text file
	private FileActions fileActions;
	private static final int FEEDBACK_RECORD_LENGTH = 6;
	private String doctorId; // to store doctor info passed from login
	private List<String[]> allDoctorFeedbacks = new ArrayList<>(); // to store all feedback for the doctor

	// path for images and text files
	private static final String FEEDBACK_FILE_PATH = "feedback.txt";
	private static final String STAR_IMAGE_PATH = "src/image/star.png";
	private static final String STAR_GLOW_IMAGE_PATH = "src/image/star-glow.png";
	private static final int RATING_COUNT = 5;  // for the total number of stars to display

	
	// constructor for view feedback
	public ViewFeedback(String[] staffDetails) {
		// retrieve the doctor ID
		if (staffDetails != null && staffDetails.length > 0) {
			this.doctorId = staffDetails[0];
		} else {
			JOptionPane.showMessageDialog(this, "User information not provided. Cannot display feedbacks.",
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.fileActions = new FileActions(FEEDBACK_FILE_PATH);

		this.setLayout(new BorderLayout());

		// filter checkboxes and button panel
		filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Feedbacks by Date"));
		todayFilter = new JCheckBox("Today");
		thisWeekFilter = new JCheckBox("This Week");
		thisMonthFilter = new JCheckBox("This Month");
		thisYearFilter = new JCheckBox("This Year");
		showAllFilter = new JCheckBox("Show All");
		clearButton = new JButton("Clear Filter");

		// group the checkboxes to make them mutually exclusive
		filterGroup = new ButtonGroup();
		filterGroup.add(todayFilter);
		filterGroup.add(thisWeekFilter);
		filterGroup.add(thisMonthFilter);
		filterGroup.add(thisYearFilter);
		filterGroup.add(showAllFilter);

		// add checkboxes and button to filter panel
		filterPanel.add(new JLabel("Filter by Date:"));
		filterPanel.add(todayFilter);
		filterPanel.add(thisWeekFilter);
		filterPanel.add(thisMonthFilter);
		filterPanel.add(thisYearFilter);
		filterPanel.add(showAllFilter);
		filterPanel.add(clearButton);

		// star rating panel
		starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
		starPanel.setBackground(new Color(240, 240, 240));

		// combine filter and star panels into a single top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(filterPanel, BorderLayout.NORTH);
		topPanel.add(starPanel, BorderLayout.CENTER);

		// table setup
		String[] columnNames = {"Feedback ID", "Customer ID", "Rating", "Message", "Doctor ID", "Date"};
		// to make the cells in the table unable to edit
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		feedbackTable = new JTable(tableModel);
		feedbackTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(feedbackTable);

		// add scroll panel and top panel to main frame
		this.add(topPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);

		// event listeners for filters
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

		// load data and initial display
		loadAllFeedbackForDoctor();
		if (allDoctorFeedbacks.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No feedback records found for this doctor.",
				"No Data Found", JOptionPane.INFORMATION_MESSAGE);
			updateTableAndStars(new ArrayList<>()); // ensure UI is still initialized even if it is empty
		} else {
			// show all data
			updateTableAndStars(allDoctorFeedbacks);
			showAllFilter.setSelected(true); // default to show all
		}
	}


	// reads the feedback file, filters by the doctor's ID, and stores the results
	private void loadAllFeedbackForDoctor() {
		allDoctorFeedbacks.clear();
		List<String[]> allFeedbacks = fileActions.returnAllDataFromFile(FEEDBACK_RECORD_LENGTH);
		if (allFeedbacks.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error reading feedback file or file is empty.",
				"File Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		for (String[] feedback : allFeedbacks) {
			if (feedback.length >= FEEDBACK_RECORD_LENGTH) {
				String staffOrDoctorId = feedback[4];
				if (staffOrDoctorId.equals(this.doctorId)) {
					allDoctorFeedbacks.add(feedback);
				}
			}
		}
	}

	
	// filter the feedback data based on given time period
	// @return a list of feedback records that match the filter criteria
	private List<String[]> filterFeedbacks(String filterType) {
		List<String[]> filteredList = new ArrayList<>();
		LocalDate now = LocalDate.now();

		for (String[] feedback : allDoctorFeedbacks) {
			try {
				LocalDate feedbackDate = LocalDate.parse(feedback[5]);
				boolean matches = false;

				switch (filterType) {
					case "today":
						matches = feedbackDate.isEqual(now);
						break;
					case "week":
						// get the week of the year based on the system's locale
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
				// skip records that have invalid date format
				System.err.println("Skipping feedback record due to invalid date format: " + feedback[5]);
			}
		}
		return filteredList;
	}


	// calculates the average rating from a list of feedback records
	// @return: calculated average rating (return 0 if the list is empty or no valid ratings are found)
	private double calculateAverageRating(List<String[]> feedbacks) {
		if (feedbacks.isEmpty()) {
			return 0.0;
		}

		int totalRating = 0;
		int validRatingsCount = 0;

		for (String[] feedback : feedbacks) {
			try {
				String ratingStr = feedback[2];
				if (ratingStr != null && !ratingStr.equalsIgnoreCase("null") && !ratingStr.trim().isEmpty()) {
					int rating = Integer.parseInt(ratingStr);
					totalRating += rating;
					validRatingsCount++;
				}
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				// skip records with invalid rating format
				System.err.println("Skipping feedback record due to invalid rating: " + feedback[2]);
			}
		}

		return validRatingsCount > 0 ? (double) totalRating / validRatingsCount : 0.0;
	}


	// updates the star rating display panel based on a given rating
	private void updateStarPanel(double rating) {
		starPanel.removeAll();
		starPanel.add(new JLabel("Average Rating: "));

		// load star icons
		ImageIcon starIcon = new ImageIcon(STAR_IMAGE_PATH);
		ImageIcon glowStarIcon = new ImageIcon(STAR_GLOW_IMAGE_PATH);

		// star
		Image starImage = starIcon.getImage();
		Image newStarImage = starImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		starIcon = new ImageIcon(newStarImage);

		// glowstar
		Image glowStarImage = glowStarIcon.getImage();
		Image newGlowStarImage = glowStarImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		glowStarIcon = new ImageIcon(newGlowStarImage);

		// display glowstar based on the average rating
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

	
	// update both the table and the star panel with new data
	private void updateTableAndStars(List<String[]> feedbacks) {
		updateTable(feedbacks);
		double avgRating = calculateAverageRating(feedbacks);
		updateStarPanel(avgRating);
	}


	// updates the JTable's model with a new set of data
	private void updateTable(List<String[]> feedbacks) {
		tableModel.setRowCount(0); // Clear existing table data
		if (feedbacks.isEmpty()) {
			tableModel.addRow(new Object[]{"No feedbacks found", "", "", "", "", ""});
		} else {
			for (String[] feedback : feedbacks) {
				tableModel.addRow(feedback);
			}
		}
	}
}
