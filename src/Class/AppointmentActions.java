package Class;

import Class.FileActions;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.io.IOException;
import java.time.LocalDate;

public class AppointmentActions {

	private final FileActions appointmentFile;
	private final FileActions paymentFile;
	private final FileActions commentFile;
	private final IDGenerator paymentIdGenerator;
	private final IDGenerator commentIdGenerator;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public AppointmentActions(FileActions appointmentFile, FileActions paymentFile, FileActions commentFile, IDGenerator paymentIdGenerator, IDGenerator commentIdGenerator) {
		this.appointmentFile = appointmentFile;
		this.paymentFile = paymentFile;
		this.commentFile = commentFile;
		this.paymentIdGenerator = paymentIdGenerator;
		this.commentIdGenerator = commentIdGenerator;
	}

	public List<String[]> getAppointmentsList(String doctorId, String range) {
		List<String[]> filteredAppointments = new ArrayList<>();
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime startWindow;
		LocalDateTime endWindow;

		switch (range) {
			case "Today":
				startWindow = currentTime.toLocalDate().atStartOfDay();
				endWindow = startWindow.plusDays(1);
				break;
			case "This week":
				LocalDate today = currentTime.toLocalDate();
				startWindow = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
				endWindow = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1).atStartOfDay();
				break;
			case "This month":
				startWindow = currentTime.toLocalDate().withDayOfMonth(1).atStartOfDay();
				endWindow = startWindow.plusMonths(1);
				break;
			case "This year":
				startWindow = currentTime.toLocalDate().withDayOfYear(1).atStartOfDay();
				endWindow = startWindow.plusYears(1);
				break;
			default: //for "All"
				startWindow = LocalDateTime.MIN;
				endWindow = LocalDateTime.MAX;
				break;
		}

		List<String[]> allAppointments = appointmentFile.returnAllDataFromFile(8);
		for (String[] appointment : allAppointments) {
			try {
				LocalDateTime appointmentDateTime = LocalDateTime.parse(appointment[4], formatter);
				if (appointment[1].equals(doctorId) && !appointmentDateTime.isBefore(startWindow) && appointmentDateTime.isBefore(endWindow)) {
					filteredAppointments.add(appointment);
				}
			} catch (Exception e) {
				System.err.println("Error parsing date for appointment: " + Arrays.toString(appointment));
			}
		}
		return filteredAppointments;
	}

	public void updateAppointment(String appointmentId, String payment, String comment) {
		List<String[]> allAppointments = appointmentFile.returnAllDataFromFile(8);
		String[] oldAppointmentData = null;
		for (String[] data : allAppointments) {
			if (data[0].equals(appointmentId)) {
				oldAppointmentData = data;
				break;
			}
		}

		if (oldAppointmentData == null) {
			return;
		}

		String[] newAppointmentData = oldAppointmentData.clone();
		newAppointmentData[5] = "complete";

		String paymentId = newAppointmentData[6].trim();
		if (paymentId.equalsIgnoreCase("N/A")) {
			if (payment != null && !payment.isEmpty()) {
				paymentId = paymentIdGenerator.generateNextId();
				String[] newPaymentData = {paymentId, payment, newAppointmentData[1], "completed"};
				paymentFile.addRowToFile(newPaymentData);
			}
		} else {
			List<String[]> allPaymentData = paymentFile.returnAllDataFromFile(4);
			for (String[] data : allPaymentData) {
				if (data[0].equals(paymentId)) {
					String[] newPaymentData = data.clone();
					newPaymentData[1] = payment;
					newPaymentData[3] = "completed";
					paymentFile.editRowFromFile(4, data, newPaymentData);
					break;
				}
			}
		}
		newAppointmentData[6] = paymentId;

		String commentId = newAppointmentData[7].trim();
		if (commentId.equalsIgnoreCase("null")) {
			if (comment != null && !comment.isEmpty()) {
				commentId = commentIdGenerator.generateNextId();
				String[] newCommentData = {commentId, comment, newAppointmentData[3]};
				commentFile.addRowToFile(newCommentData);
			}
		} else {
			List<String[]> allCommentData = commentFile.returnAllDataFromFile(3);
			for (String[] data : allCommentData) {
				if (data[0].equals(commentId)) {
					String[] newCommentData = data.clone();
					newCommentData[1] = comment;
					commentFile.editRowFromFile(3, data, newCommentData);
					break;
				}
			}
		}
		newAppointmentData[7] = commentId;

		appointmentFile.editRowFromFile(8, oldAppointmentData, newAppointmentData);
	}

	public Map<String, String> getPaymentDataMap() {
		Map<String, String> paymentMap = new HashMap<>();
		List<String[]> allPayments = paymentFile.returnAllDataFromFile(4);
		for (String[] payment : allPayments) {
			if (payment.length >= 2) {
				paymentMap.put(payment[0], payment[1]);
			}
		}
		return paymentMap;
	}

	public Map<String, String> getCommentDataMap() {
		Map<String, String> commentMap = new HashMap<>();
		List<String[]> allComments = commentFile.returnAllDataFromFile(3);
		for (String[] comment : allComments) {
			if (comment.length >= 2) { 
				commentMap.put(comment[0], comment[1]);
			}
		}
		return commentMap;
	}
}
