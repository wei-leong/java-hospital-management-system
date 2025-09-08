package Doctor;

import Class.Doctor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class ViewFeedback extends JPanel {

	// GUI component
	private JTable feedbackTable;
	private DefaultTableModel tableModel;
	private JPanel filterPanel;
	private JPanel starPanel;

	private JCheckBox todayFilter, thisWeekFilter, thisMonthFilter, thisYearFilter, showAllFilter;
	private ButtonGroup filterGroup;

	private String doctorId;
	private List<String[]> allDoctorFeedbacks = new ArrayList<>();

	private Doctor doctor;

	public ViewFeedback(String[] staffDetails, Doctor doctor) {
		if (staffDetails != null && staffDetails.length > 0) {
			this.doctorId = staffDetails[0];
		} else {
			JOptionPane.showMessageDialog(this, "User information not provided. Cannot display feedbacks.",
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.doctor = doctor;
		this.setLayout(new BorderLayout());

		// table model setup
		String[] columnNames = {"Customer Name", "Customer Email", "Date", "Comments", "Staff Name", "Staff ID", "Rating"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		feedbackTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(feedbackTable);
		this.add(scrollPane, BorderLayout.CENTER);

		// filter panel
		filterPanel = new JPanel();
		filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filter by Date"));

		// checkboxes
		todayFilter = new JCheckBox("Today");
		thisWeekFilter = new JCheckBox("This Week");
		thisMonthFilter = new JCheckBox("This Month");
		thisYearFilter = new JCheckBox("This Year");
		showAllFilter = new JCheckBox("Show All");
		showAllFilter.setSelected(true); // default selected

		// button group to make checkboxes mutually exclusive
		filterGroup = new ButtonGroup();
		filterGroup.add(todayFilter);
		filterGroup.add(thisWeekFilter);
		filterGroup.add(thisMonthFilter);
		filterGroup.add(thisYearFilter);
		filterGroup.add(showAllFilter);

		filterPanel.add(todayFilter);
		filterPanel.add(thisWeekFilter);
		filterPanel.add(thisMonthFilter);
		filterPanel.add(thisYearFilter);
		filterPanel.add(showAllFilter);

		todayFilter.addItemListener(e -> handleFilter(e, "Today"));
		thisWeekFilter.addItemListener(e -> handleFilter(e, "This Week"));
		thisMonthFilter.addItemListener(e -> handleFilter(e, "This Month"));
		thisYearFilter.addItemListener(e -> handleFilter(e, "This Year"));
		showAllFilter.addItemListener(e -> handleFilter(e, "Show All"));

		// star rating panel
		starPanel = new JPanel();
		starPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		starPanel.setBorder(BorderFactory.createTitledBorder("Average Rating"));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(filterPanel);
		topPanel.add(starPanel);

		this.add(topPanel, BorderLayout.NORTH);

		loadAllFeedbackForDoctor();
	}

	private void handleFilter(ItemEvent e, String filterType) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			List<String[]> filteredData = new ArrayList<>();
			LocalDate today = LocalDate.now();
			switch (filterType) {
				case "Today":
					filteredData = filterByDate(today, today);
					break;
				case "This Week":
					LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
					LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
					filteredData = filterByDate(startOfWeek, endOfWeek);
					break;
				case "This Month":
					LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
					LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
					filteredData = filterByDate(startOfMonth, endOfMonth);
					break;
				case "This Year":
					LocalDate startOfYear = today.with(TemporalAdjusters.firstDayOfYear());
					LocalDate endOfYear = today.with(TemporalAdjusters.lastDayOfYear());
					filteredData = filterByDate(startOfYear, endOfYear);
					break;
				case "Show All":
				default:
					filteredData = allDoctorFeedbacks;
					break;
			}
			updateTableAndStars(filteredData);
		}
	}

	private List<String[]> filterByDate(LocalDate startDate, LocalDate endDate) {
		List<String[]> filteredList = new ArrayList<>();
		for (String[] feedback : allDoctorFeedbacks) {
			try {
				LocalDate feedbackDate = LocalDate.parse(feedback[2]);
				if (!feedbackDate.isBefore(startDate) && !feedbackDate.isAfter(endDate)) {
					filteredList.add(feedback);
				}
			} catch (DateTimeParseException e) {
				System.err.println("Invalid date format in feedback data: " + feedback[2]);
			}
		}
		return filteredList;
	}

	private void loadAllFeedbackForDoctor() {
		allDoctorFeedbacks = doctor.returnFeedbackList();
		updateTableAndStars(allDoctorFeedbacks);
	}

	private void updateTable(List<String[]> feedbacks) {
		tableModel.setRowCount(0);
		for (String[] feedback : feedbacks) {
			tableModel.addRow(feedback);
		}
	}

	private void updateStarPanel(double rating) {
		starPanel.removeAll();
		starPanel.add(new JLabel("Average Rating: " + String.format("%.1f", rating)));

		ImageIcon starIcon = new ImageIcon(getClass().getResource("/image/star.png"));
		ImageIcon glowStarIcon = new ImageIcon(getClass().getResource("/image/star-glow.png"));

		Image starImage = starIcon.getImage();
		Image newStarImage = starImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		starIcon = new ImageIcon(newStarImage);

		Image glowStarImage = glowStarIcon.getImage();
		Image newGlowStarImage = glowStarImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		glowStarIcon = new ImageIcon(newGlowStarImage);

		int glowStars = (int) Math.round(rating);

		for (int i = 0; i < 5; i++) {
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

	private double calculateAverageRating(List<String[]> feedbacks) {
		if (feedbacks.isEmpty()) {
			return 0.0;
		}

		double totalRating = 0;
		for (String[] feedback : feedbacks) {
			totalRating += Double.parseDouble(feedback[6]);
		}
		return totalRating / feedbacks.size();
	}

	private void updateTableAndStars(List<String[]> feedbacks) {
		updateTable(feedbacks);
		double avgRating = calculateAverageRating(feedbacks);
		updateStarPanel(avgRating);
	}
}
