package Class;

import Class.AppointmentActions;
import Class.FeedbackActions;
import Class.MedicineActions;
import Class.WorkTimeActions;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class Doctor extends Person {

	private final AppointmentActions appointmentActions;
	private final FeedbackActions feedbackActions;
	private final MedicineActions medicineActions;
	private final WorkTimeActions workTimeActions;

	private final String[] _ownProfile;

	// constructor
	public Doctor(String email, String password, AppointmentActions appointmentActions, FeedbackActions feedbackActions, MedicineActions medicineActions, WorkTimeActions workTimeActions) {
		super(email, password);
		this._ownProfile = null;
		this.appointmentActions = appointmentActions;
		this.feedbackActions = feedbackActions;
		this.medicineActions = medicineActions;
		this.workTimeActions = workTimeActions;
	}

	public Doctor(String[] ownProfile, AppointmentActions appointmentActions, FeedbackActions feedbackActions, MedicineActions medicineActions, WorkTimeActions workTimeActions) {
		super(ownProfile[0], null);
		this._ownProfile = ownProfile;
		this.appointmentActions = appointmentActions;
		this.feedbackActions = feedbackActions;
		this.medicineActions = medicineActions;
		this.workTimeActions = workTimeActions;
		
		// JOptionPane.showMessageDialog(null, ownProfile[0].toString());
	}

	// public methods to assign tasks to the injected dependencies
	public List<String[]> returnAppointmentsList(String range) {
		return appointmentActions.getAppointmentsList(_ownProfile[0], range);
	}

	public void updateAppointment(String appointmentId, String payment, String comment) {
		appointmentActions.updateAppointment(appointmentId, payment, comment);
	}

	public List<String[]> returnFeedbackList() {
		return feedbackActions.returnRatingList(_ownProfile[0]);
	}

	public double getAverageRating(String doctorId) {
		return feedbackActions.returnAverageRating(doctorId);
	}

	public void updateWorkTime(String startTime, String endTime) {
		workTimeActions.updateWorkTime(_ownProfile[0], startTime, endTime);
	}

	public String[] getCurrentWorkTime() {
		return workTimeActions.getCurrentWorkTime(_ownProfile[0]);
	}

	public void addMedicine(String id, String name, String price) {
		medicineActions.addMedicine(id, name, price);
	}

	public void editMedicine(String oldId, String newId, String newName, String newPrice) {
		medicineActions.editMedicine(oldId, newId, newName, newPrice);
	}

	public void deleteMedicine(String id) {
		medicineActions.deleteMedicine(id);
	}

	public List<String[]> returnMedicineList() {
		return medicineActions.returnMedicineList();
	}

	public Map<String, String> getPaymentDetails() {
		return this.appointmentActions.getPaymentDataMap();
	}

	public Map<String, String> getCommentDetails() {
		return this.appointmentActions.getCommentDataMap();
	}
}
