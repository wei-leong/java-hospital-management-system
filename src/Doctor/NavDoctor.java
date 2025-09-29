package Doctor;

import Class.*;
import javax.swing.*;

public class NavDoctor extends BasicNav {

	private final ImageScaler imgScale = new ImageScaler();

	// pages
	private final CheckAppointment checkAppointment;
	private final DoctorWorkTime doctorWorkTime;
	private final ManageMedicine manageMedicine;
	private final ViewFeedback viewFeedback;

	// string identifiers for each page
	private final String checkAppointmentStr = "Check Appointment";
	private final String doctorWorkTimeStr = "Manage Working Time";
	private final String manageMedicineStr = "Manage Medicine";
	private final String viewFeedbackStr = "View Feedback";

	// icons for each page
	private final Icon _iconAppointment;
	private final Icon _iconWorkTime;
	private final Icon _iconMedicine;
	private final Icon _iconFeedback;

	private Doctor doctor;

	public NavDoctor(String[] staffDetails, JFrame loginForm) {
		super("APU Medical Centre", staffDetails, loginForm);

		// define all actions dependencies here
		FileActions appointmentFile = new FileActions("appointment.txt");
		FileActions paymentFile = new FileActions("payment.txt");
		FileActions commentFile = new FileActions("comments.txt");
		IDGenerator paymentIdGenerator = new IDGenerator("P", "payment.txt");
		IDGenerator commentIdGenerator = new IDGenerator("G", "comments.txt");
		AppointmentActions appointmentActions = new AppointmentActions(appointmentFile, paymentFile, commentFile, paymentIdGenerator, commentIdGenerator);

		FeedbackActions feedbackActions = new FeedbackActions();

		FileActions medicineFile = new FileActions("medicine.txt");
		IDGenerator medicineIdGenerator = new IDGenerator("Q", "medicine.txt");
		MedicineActions medicineActions = new MedicineActions(medicineFile, medicineIdGenerator, "medicine.txt");

		FileActions workTimeFile = new FileActions("doctor_worktime.txt");
		WorkTimeActions workTimeActions = new WorkTimeActions(workTimeFile);

		// create a doctor instance to hold all actions
		this.doctor = new Doctor(staffDetails, appointmentActions, feedbackActions, medicineActions, workTimeActions);

		// define page icons
		this._iconAppointment = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);
		this._iconWorkTime = imgScale.returnScaledImageIcon("/image/worktime.png", 25, 25);
		this._iconFeedback = imgScale.returnScaledImageIcon("/image/view_feedback.png", 25, 25);
		this._iconMedicine = imgScale.returnScaledImageIcon("/image/medicine.png", 25, 25);

		// pages
		this.checkAppointment = new CheckAppointment(staffDetails, this.doctor);
		this.doctorWorkTime = new DoctorWorkTime(staffDetails, this.doctor);
		this.manageMedicine = new ManageMedicine(this.doctor);
		this.viewFeedback = new ViewFeedback(staffDetails, this.doctor);

		// add pages (method from basicnav)
		addPage(checkAppointmentStr, checkAppointment, _iconAppointment);
		addPage(doctorWorkTimeStr, doctorWorkTime, _iconWorkTime);
		addPage(manageMedicineStr, manageMedicine, _iconMedicine);
		addPage(viewFeedbackStr, viewFeedback, _iconFeedback);

		// Default page
		titleChanger(checkAppointmentStr);
		cards.show(content, checkAppointmentStr);

		setVisible(true);
	}
}
