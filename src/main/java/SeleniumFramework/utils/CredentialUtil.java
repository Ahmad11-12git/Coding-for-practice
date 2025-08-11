package SeleniumFramework.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CredentialUtil {

    private static final int MAX_RECORDS = 3;

    /* -------------------- CORE CSV OPERATIONS -------------------- */

    private static List<String[]> readCSV(String filePath, int expectedColumns) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (expectedColumns == 0 || parts.length == expectedColumns) {
                    data.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static List<String> readSingleColumnCSV(String filePath) {
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    records.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static void writeCSV(String filePath, List<String[]> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] record : records) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeSingleColumnCSV(String filePath, List<String> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String record : records) {
                bw.write(record);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> void trimToLast(List<T> list, int maxSize) {
        if (list.size() > maxSize) {
            list.subList(0, list.size() - maxSize).clear();
        }
    }

    /* -------------------- NEW GENERIC METHODS -------------------- */

    public static String[] readLatestCredentials(String filePath) {
        List<String[]> records = readCSV(filePath, 3);
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }

    public static void updateLatestPassword(String filePath, String name, String userId, String newPassword) {
        List<String[]> records = readCSV(filePath, 3);
        if (!records.isEmpty()) {
            records.set(records.size() - 1, new String[]{name, userId, newPassword});
            writeCSV(filePath, records);
        }
    }

    public static void appendToCSV(String filePath, String col1, String col2, String col3) {
        List<String[]> records = readCSV(filePath, 3);
        records.add(new String[]{col1, col2, col3});
        trimToLast(records, MAX_RECORDS);
        writeCSV(filePath, records);
    }

    public static void appendSingleColumn(String filePath, String value) {
        List<String> records = readSingleColumnCSV(filePath);
        records.add(value);
        trimToLast(records, MAX_RECORDS);
        writeSingleColumnCSV(filePath, records);
    }

    public static String readLastFromSingleColumn(String filePath) {
        List<String> records = readSingleColumnCSV(filePath);
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }

    public static void saveLatestToCSV(String filePath, String col1, String col2, String col3) {
        List<String[]> records = readCSV(filePath, 3);
        trimToLast(records, MAX_RECORDS - 1);
        records.add(new String[]{col1, col2, col3});
        writeCSV(filePath, records);
    }

    public static String[] readLatestFullDetails(String filePath) {
        return readLatestCredentials(filePath);
    }

    public static String generateRandomUTR() {
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000);
        return "UTR" + randomNum + System.currentTimeMillis();
    }

    /* -------------------- OLD METHOD NAMES (COMPATIBILITY LAYER) -------------------- */

    public static String[] readLatestCredentialsSuperUser(String filePath) {
        return readLatestCredentials(filePath);
    }

    public static String[] readLatestCredentialsEntityUser(String filePath) {
        return readLatestCredentials(filePath);
    }

    public static void updateLatestPasswordSuperUser(String filePath, String userFullName, String userId, String newPassword) {
        updateLatestPassword(filePath, userFullName, userId, newPassword);
    }

    public static void updateLatestPasswordEntity(String filePath, String entityName, String userId, String newPassword) {
        updateLatestPassword(filePath, entityName, userId, newPassword);
    }

    public static void appendBankNameToCSV(String filePath, String bankName) {
        appendSingleColumn(filePath, bankName);
    }

    public static void appendEntityNameToCSV(String filePath, String entityName) {
        appendSingleColumn(filePath, entityName);
    }

    public static String readLatestEntityName(String filePath) {
        return readLastFromSingleColumn(filePath);
    }

    public static String readLatestBankName(String filePath) {
        return readLastFromSingleColumn(filePath);
    }

    public static void writeToCSV(String filePathUID, String user_Id) {
        appendSingleColumn(filePathUID, user_Id);
    }

    public static String readLastUserIdFromCSV(String filePath) {
        return readLastFromSingleColumn(filePath);
    }
}
