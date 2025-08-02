package Manager;

import Class.Manager;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import javax.swing.*;

public class Feedback extends JPanel {

    // Made the feedback JPanel scrollable
    // Made the feedback JPanel fixed height
    public Feedback() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        Manager managerActions = new Manager();

        // ——— 1) Review Summary ———
        JLabel lblSummaryTitle = new JLabel("Review Summary");
        lblSummaryTitle.setFont(lblSummaryTitle.getFont().deriveFont(Font.BOLD, 18f));
        lblSummaryTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 0));
        lblSummaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel summary = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        summary.setBackground(Color.WHITE);
        summary.setAlignmentX(Component.LEFT_ALIGNMENT);
        int staffAvg = managerActions.FeedbackSummary("S");        
        int doctorAvg = managerActions.FeedbackSummary("D");

        summary.add(makeAverageBlock(String.valueOf(staffAvg), "To Doctor"));
        summary.add(makeAverageBlock(String.valueOf(doctorAvg), "To Staff"));

        // wrap title + summary in a left‐aligned Box
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.add(lblSummaryTitle);
        summaryPanel.add(summary);

        // ——— 2) Tag filters ———
        JPanel tags = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        tags.setBackground(Color.WHITE);
        tags.setAlignmentX(Component.LEFT_ALIGNMENT);
        JCheckBox cbDoc = new JCheckBox("Doctor");
        JCheckBox cbStaff = new JCheckBox("Staff");
        for (JCheckBox cb : List.of(cbDoc, cbStaff)) {
            cb.setBackground(Color.WHITE);
            cb.setFocusPainted(false);
            tags.add(cb);
            cb.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    for (JCheckBox other : List.of(cbDoc, cbStaff)) {
                        if (other != cb) {
                            other.setSelected(false);
                        }
                    }
                }
            });
        }

        // put summaryPanel + tags into a single NORTH container
        JPanel north = new JPanel();
        north.setBackground(Color.WHITE);
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(summaryPanel);
        north.add(tags);
        add(north, BorderLayout.NORTH);

        // ——— 3) Feedback title + scrollable comments ———
        JLabel lblFeedbackTitle = new JLabel("Feedback");
        lblFeedbackTitle.setFont(lblFeedbackTitle.getFont().deriveFont(Font.BOLD, 18f));
        lblFeedbackTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 0));
        lblFeedbackTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel commentList = new JPanel();
        commentList.setBackground(Color.WHITE);
        commentList.setLayout(new BoxLayout(commentList, BoxLayout.Y_AXIS));
        commentList.setAlignmentX(Component.LEFT_ALIGNMENT);

        // sample data…
        List<Review> reviews = List.of(
                new Review("Alice", "alice@mail", "12/03/2025", "Great job", "Doctor", "DOC001", "5"),
                new Review("Bob", "bob@mail", "11/03/2025", "Okay", "Staff", "STF002", "3"),
                new Review("Alice", "alice@mail", "12/03/2025", "Great job", "Doctor", "DOC001", "5"),
                new Review("Bob", "bob@mail", "11/03/2025", "Okay", "Staff", "STF002", "3"),
                new Review("Alice", "alice@mail", "12/03/2025", "Great job", "Doctor", "DOC001", "5"),
                new Review("Bob", "bob@mail", "11/03/2025", "Okay", "Staff", "STF002", "3")
        );
        for (Review r : reviews) {
            JPanel card = makeCommentCard(r);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            commentList.add(card);
            commentList.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scroll = new JScrollPane(commentList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // put feedback title + scroll into center box
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(lblFeedbackTitle);
        center.add(scroll);
        add(center, BorderLayout.CENTER);
    }

    // ─── builds one “average rating” block ─────────────────────────
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
        p.add(lblTitle,BorderLayout.SOUTH);
        
        return p;
    }

    private JPanel makeCommentCard(Review r) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        // 1) name + role + id
        JPanel pName = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JLabel name = new JLabel(r.name + " → " + r.targetRole + " (" + r.id + ")");
        name.setFont(name.getFont().deriveFont(Font.BOLD, 20f));
        pName.setBackground(Color.WHITE);
        pName.add(name);
        content.add(pName);

        // 2) email
        JPanel pEmail = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JLabel email = new JLabel(r.email);
        email.setFont(email.getFont().deriveFont(Font.PLAIN, 18f));
        pEmail.setBackground(Color.WHITE);
        pEmail.add(email);
        content.add(pEmail);

        // 3) rating + date
        JPanel rd = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        rd.setBackground(Color.WHITE);

        // load and scale stars
        ImageIcon rawOn = new ImageIcon(getClass().getResource("/image/star-glow.png"));
        ImageIcon on16 = new ImageIcon(rawOn.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        ImageIcon rawOff = new ImageIcon(getClass().getResource("/image/star.png"));
        ImageIcon off16 = new ImageIcon(rawOff.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        int rating = Integer.parseInt(r.rating);
        for (int i = 1; i <= 5; i++) {
            rd.add(new JLabel(i <= rating ? on16 : off16));
        }
        rd.add(Box.createHorizontalStrut(8));
       
        JLabel date = new JLabel(r.date);
        date.setFont(date.getFont().deriveFont(Font.PLAIN, 18f));
        rd.add(date);

        content.add(Box.createRigidArea(new Dimension(0, 4)));
        content.add(rd);

        // 4) the feedback text (wrap at ~400px)
        JPanel pComment = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JLabel comment = new JLabel(r.text);
        comment.setFont(comment.getFont().deriveFont(Font.PLAIN, 20f));
        comment.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        pComment.setBackground(Color.WHITE);
        pComment.add(comment);
        content.add(pComment);

        // now add content to the card
        card.add(content, BorderLayout.WEST);
        return card;
    }

    private static class Review {

        String name, email, date, text, targetRole, id, rating;

        Review(String n, String e, String d, String t, String role, String id, String r) {
            name = n;
            email = e;
            date = d;
            text = t;
            targetRole = role;
            this.id = id;
            rating = r;
        }
    }
}
