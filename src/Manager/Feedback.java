package Manager;

import Class.Manager;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import javax.swing.*;

public class Feedback extends JPanel {

    private JPanel commentList;
    private String currentFilter = "All";
    private final Manager managerActions = new Manager();

    public Feedback() {
        // JPanel Settings
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        add(reviewSummary(), BorderLayout.NORTH); // Review Summary Section ( Review Summary Title + Avg Rating for Staff / Doctor
        add(customerComments(), BorderLayout.CENTER); // Customer Comments Section ( Feedback Title + Comments from Custoemr to Doctor / Staff 
        
        refreshComments(); // Initial table format
    }

    private JPanel reviewSummary() {
        // NORTH JPanel to add Review Summary
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.setBackground(Color.WHITE);

        // Title : Review Summary
        north.add(reviewSummaryTitle("Review Summary",18f,0,10,8,0));

        // Blocks : Overall Average Rating Blocks from Staff and Doctor
        north.add(reviewSummaryRating());
        return north;
    }

    private JLabel reviewSummaryTitle(String title, float textSize,int top, int left, int bottom, int right) {
        JLabel lblSummaryTitle = new JLabel(title);
        lblSummaryTitle.setFont(lblSummaryTitle.getFont().deriveFont(Font.BOLD, textSize));
        lblSummaryTitle.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        lblSummaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lblSummaryTitle;
    }

    private JPanel reviewSummaryRating() {
        JPanel summaryBlocks = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        summaryBlocks.setBackground(Color.WHITE);
        summaryBlocks.setAlignmentX(Component.LEFT_ALIGNMENT);

        int staffAvg = managerActions.FeedbackSummary("S");
        int doctorAvg = managerActions.FeedbackSummary("D");
        summaryBlocks.add(makeAverageBlock(String.valueOf(staffAvg), "To Doctor"));
        summaryBlocks.add(makeAverageBlock(String.valueOf(doctorAvg), "To Staff"));
        return summaryBlocks;
    }

    private JPanel customerComments() {
        // CENTER: Feedback title, then tags, then scrollable comments
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);

        // 1) “Feedback” heading
        center.add(reviewSummaryTitle("Feedback",18f,8,10,8,0));

        // 2) Tag filters just below the title
        JPanel tags = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        tags.setBackground(Color.WHITE);
        tags.setAlignmentX(Component.LEFT_ALIGNMENT);
        JCheckBox cbAll = new JCheckBox("All", true);
        JCheckBox cbStaff = new JCheckBox("Staff", false);
        JCheckBox cbDoc = new JCheckBox("Doctor", false);
        for (JCheckBox cb : List.of(cbAll, cbStaff, cbDoc)) {
            cb.setBackground(Color.WHITE);
            tags.add(cb);
            cb.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    for (JCheckBox other : List.of(cbAll, cbStaff, cbDoc)) {
                        if (other != cb) {
                            other.setSelected(false);
                        }
                    }
                    currentFilter = cb.getText().equals("All") ? "All"
                            : cb.getText().equals("Staff") ? "S"
                            : "D";
                    refreshComments();
                }
            });
        }
        center.add(tags);

        // 3) Scrollable list of comments
        commentList = new JPanel();
        commentList.setLayout(new BoxLayout(commentList, BoxLayout.Y_AXIS));
        commentList.setBackground(Color.WHITE);
        commentList.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scroll = new JScrollPane(
                commentList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(0, 400));

        center.add(scroll);
        return center;
    }

    // Average Rating Block
    private JComponent makeAverageBlock(String avg, String title) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(Color.WHITE);

        JLabel num = new JLabel(avg, SwingConstants.CENTER);
        num.setFont(num.getFont().deriveFont(Font.BOLD, 70f));
        p.add(num, BorderLayout.NORTH);

        JPanel stars = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        stars.setBackground(Color.WHITE);
        ImageIcon on = new ImageIcon(getClass().getResource("/image/star-glow.png"));
        ImageIcon off = new ImageIcon(getClass().getResource("/image/star.png"));
        ImageIcon on16 = new ImageIcon(on.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        ImageIcon off16 = new ImageIcon(off.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        int r = Math.round(Float.parseFloat(avg));
        for (int i = 1; i <= 5; i++) {
            stars.add(new JLabel(i <= r ? on16 : off16));
        }
        p.add(stars, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.PLAIN, 15f));
        p.add(lblTitle, BorderLayout.SOUTH);

        return p;
    }

    private JPanel makeCommentCard(String[] r) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel name = new JLabel(r[0] + " → " + r[4] + " (" + r[5] + ")");
        name.setFont(name.getFont().deriveFont(Font.BOLD, 14f));

        JLabel email = new JLabel(r[1]);
        email.setFont(email.getFont().deriveFont(Font.PLAIN, 12f));

        // stars + date
        JPanel rd = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        rd.setBackground(Color.WHITE);
        ImageIcon on16 = new ImageIcon(new ImageIcon(
                getClass().getResource("/image/star-glow.png"))
                .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        ImageIcon off16 = new ImageIcon(new ImageIcon(
                getClass().getResource("/image/star.png"))
                .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        int rating = Integer.parseInt(r[6]);
        for (int i = 1; i <= 5; i++) {
            rd.add(new JLabel(i <= rating ? on16 : off16));
        }
        rd.add(Box.createHorizontalStrut(8));
        JLabel date = new JLabel(r[2]);
        date.setFont(date.getFont().deriveFont(Font.PLAIN, 12f));
        rd.add(date);

        JLabel text = new JLabel("<html><body style='width:400px'>" + r[3] + "</body></html>");
        text.setFont(text.getFont().deriveFont(Font.PLAIN, 12f));

        // assemble
        Box box = Box.createVerticalBox();
        box.add(name);
        box.add(email);
        box.add(rd);
        box.add(text);

        card.add(box, BorderLayout.WEST);
        return card;
    }

    private void refreshComments() {
        commentList.removeAll();
        // pull each feedback row: {custName, custEmail, date, text, staffName, staffId, rating}
        List<String[]> rows = managerActions.returnFeedbackList(currentFilter);
        for (String[] r : rows) {
            commentList.add(makeCommentCard(r));
            commentList.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        commentList.revalidate();
        commentList.repaint();
    }
}
