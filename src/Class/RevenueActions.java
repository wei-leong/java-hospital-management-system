/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class RevenueActions extends FileActions {
    
    private static final int idx_id = 0;
    private static final int idx_amount = 1;
    private static final int idx_date = 2;
    private static final int idx_status = 3;
    private static final int txt_len = 4;

    public RevenueActions() {
        super("payment.txt");
    }

    public double[] returnMonthlyRevenue(int year) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        double[] totals = new double[12];
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (String[] row : allData) {
            double amount = Double.parseDouble(row[idx_amount]);
            LocalDate date = LocalDate.parse(row[idx_date], df);
            if (row[3].equals("completed")) {
                if (date.getYear() == year) {
                    int m = date.getMonthValue() - 1; // Jan=0, Feb=1, …
                    totals[m] += amount;
                }
            }

        }
        return totals;
    }

    public double[] returnYearsRevenue(int anchorYear) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int span = 10;
        int startYear = anchorYear - (span - 1);  // e.g. 2025 - 9 = 2016
        double[] totals = new double[span];

        for (String[] rows : allData) {
            double amount = Double.parseDouble(rows[idx_amount]);
            LocalDate date = LocalDate.parse(rows[idx_date], df);
            int y = date.getYear();
            int idx = y - startYear;      // 2016→0, 2017→1, …, 2025→9
            if (rows[3].equals("completed")) {
                if (0 <= idx && idx < span) {
                    totals[idx] += amount;
                }
            }
        }
        return totals;
    }
}
