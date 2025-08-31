package Doctor;

import Class.*;
import javax.swing.*;

public class NavDoctor extends BasicNav {

	private final ImageScaler imgScale = new ImageScaler();
	
	// page for check appointment
	private final CheckAppointment checkAppointment;
	private final String checkAppointmentStr = "Check Appointment";

	// page for manage working time
	private final DoctorWorkTime doctorWorkTime;
	private final String doctorWorkTimeStr = "Manage Working Time";
	
	// page for manage medicine
	private final ManageMedicine manageMedicine;
	private final String manageMedicineStr = "Manage Medicine";
	
	// page for view feedback
	private final ViewFeedback viewFeedback;
	private final String viewFeedbackStr = "View Feedback";
	
	// icons for each pages
	private final Icon _iconAppointment;	
	private final Icon _iconWorkTime;
	private final Icon _iconMedicine;
	private final Icon _iconFeedback;

	public NavDoctor(String[] staffDetails) {
		super("APU Medical Centre", staffDetails);

		// define page icon
		this._iconAppointment = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);
		this._iconWorkTime = imgScale.returnScaledImageIcon("/image/worktime.png", 25, 25);
		this._iconFeedback = imgScale.returnScaledImageIcon("/image/view_feedback.png", 25, 25);
		this._iconMedicine = imgScale.returnScaledImageIcon("/image/medicine.png", 25, 25);

		// pages
		this.checkAppointment = new CheckAppointment(staffDetails);
		this.doctorWorkTime = new DoctorWorkTime(staffDetails);
		this.manageMedicine = new ManageMedicine();
		this.viewFeedback = new ViewFeedback(staffDetails);

		// add page (method from basicnav)
		addPage(checkAppointmentStr, checkAppointment, _iconAppointment);
		addPage(doctorWorkTimeStr, doctorWorkTime, _iconWorkTime);
		addPage(manageMedicineStr, manageMedicine, _iconMedicine);
		addPage(viewFeedbackStr, viewFeedback, _iconFeedback);

		titleChanger(checkAppointmentStr);
		cards.show(content, checkAppointmentStr);

		setVisible(true);
	}
}
