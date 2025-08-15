/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            if (date.getYear() == year) {
                int m = date.getMonthValue() - 1; // Jan=0, Feb=1, …
                totals[m] += amount;
            }
        }

//        Path paymentData = Paths.get("src", "txt", "payment.txt");
//        try {
//            for (String line : Files.readAllLines(paymentData)) {
//                String[] parts = line.trim().split(",", 4);
//                double amount = Double.parseDouble(parts[1]);
//                LocalDate date = LocalDate.parse(parts[2], df);
//                if (date.getYear() == year) {
//                    int m = date.getMonthValue() - 1; // Jan=0, Feb=1, …
//                    totals[m] += amount;
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading payment.txt: " + e.getMessage());
//        }
        return totals;
    }

    public double[] returnYearsRevenue(int anchorYear) {
        List<String[]> allData = returnAllDataFromFile(txt_len);

        int span = 10;
        int startYear = anchorYear - (span - 1);  // e.g. 2025 - 9 = 2016
        double[] totals = new double[span];
        Path paymentData = Paths.get("src", "txt", "payment.txt");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            for (String line : Files.readAllLines(paymentData)) {
                String[] parts = line.trim().split(",", 4);
                double amount = Double.parseDouble(parts[1]);
                LocalDate date = LocalDate.parse(parts[2], df);
                int y = date.getYear();
                int idx = y - startYear;      // 2016→0, 2017→1, …, 2025→9
                if (0 <= idx && idx < span) {
                    totals[idx] += amount;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading payment.txt: " + e.getMessage());
        }
        return totals;
    }
}
