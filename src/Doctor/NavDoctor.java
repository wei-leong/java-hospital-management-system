package Doctor;

import Class.*;
import javax.swing.*;

public class NavDoctor extends BasicNav {

	private final ImageScaler imgScale = new ImageScaler();

	// Pages (keep as fields so subclass can interact with them)
	private final CheckAppointment checkAppointment;
	private final String checkAppointmentStr = "Check Appointment";

	private final DoctorWorkTime doctorWorkTime;
	private final String doctorWorkTimeStr = "Manage Working Time";
	
	private final ManageMedicine manageMedicine;
	private final String manageMedicineStr = "Manage Medicine";
	
	private final ViewFeedback viewFeedback;
	private final String viewFeedbackStr = "View Feedback";

	//private final Dashboard dashboard = new Dashboard();
	//private final String dashboardStr = "Dashboard ganninia";
	//private final StaffManagement staffManagement;
	//private final String staffManagementStr = "Staff Management";
	//private final Feedback feedback = new Feedback();
	//private final String feedbackStr = "View Feedback";
	//private final ViewAppointment viewAppointment = new ViewAppointment();
	//private final String viewAppointmentStr = "View Appointment";
	private final Icon _iconDashboard;
	private final Icon _iconStaffManagement;
	private final Icon _iconFeedback;
	private final Icon _iconAppointment;

	public NavDoctor(String[] staffDetails) {
		super("APU Medical Centre", staffDetails);

		// Define Page Icon
		this._iconDashboard = imgScale.returnScaledImageIcon("/image/dashboard.png", 25, 25);
		this._iconStaffManagement = imgScale.returnScaledImageIcon("/image/staff_management.png", 25, 25);
		this._iconFeedback = imgScale.returnScaledImageIcon("/image/view_feedback.png", 25, 25);
		this._iconAppointment = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);

		// not needed in doctor nav ui, this is a page for manager
		//this.staffManagement = new StaffManagement(staffDetails);
		// this one need
		this.checkAppointment = new CheckAppointment(staffDetails);
		this.doctorWorkTime = new DoctorWorkTime(staffDetails);
		this.manageMedicine = new ManageMedicine();
		this.viewFeedback = new ViewFeedback(staffDetails);

		// Add Page
		addPage(checkAppointmentStr, checkAppointment, _iconFeedback);
		addPage(doctorWorkTimeStr, doctorWorkTime, _iconFeedback);
		addPage(manageMedicineStr, manageMedicine, _iconFeedback);
		addPage(viewFeedbackStr, viewFeedback, _iconFeedback);
		//addPage(staffManagementStr, staffManagement, _iconStaffManagement);
		//addPage(feedbackStr, feedback, _iconFeedback);
		//addPage(viewAppointmentStr, viewAppointment, _iconAppointment);

		titleChanger(checkAppointmentStr);
		cards.show(content, checkAppointmentStr);

		setVisible(true);
	}
}
