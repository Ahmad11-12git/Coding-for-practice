package SeleniumFramework.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CredentialUtil {


    public static String[] readLatestCredentialsSuperUser(String filePath) {
        List<String[]> records = readCSV(filePath);
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }
    public static String[] readLatestCredentialsEntityUser(String filePath) {
        List<String[]> records = readCSV(filePath);
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }



    public static void updateLatestPasswordSuperUser(String filePath,String userFullName, String userId, String newPassword) {
        List<String[]> records = readCSV(filePath);
        if (!records.isEmpty()) {
            records.set(records.size() - 1, new String[]{userFullName, userId, newPassword});
            writeCSV(filePath, records);
        }
    }

    public static void updateLatestPasswordEntity(String filePath,String entityName, String userId, String newPassword) {
        List<String[]> records = readCSV(filePath);
        if (!records.isEmpty()) {
            records.set(records.size() - 1, new String[]{entityName, userId, newPassword});
            writeCSV(filePath, records);
        }
    }

    public static void appendToCSV(String filePath,String entityName, String userId, String password) {
        List<String[]> records = readCSV(filePath);
        records.add(new String[]{entityName,userId, password});
        if (records.size() > 3) {
            records = records.subList(records.size() - 3, records.size());
        }
        writeCSV(filePath, records);
    }

    public static void appendBankNameToCSV(String filePath, String bankName) {
        appendSingleColumn(filePath, bankName);
    }

    public static void appendEntityNameToCSV(String filePath, String entityName) {
        appendSingleColumn(filePath, entityName);
    }

//    public static List<String> readBankNamesFromCSV(String filePath) {
//        List<String> bankNames = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                bankNames.add(line.trim());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bankNames;
//    }


    private static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) data.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

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

    //-----------------------------------------------------
    private static void appendSingleColumn(String filePath, String value) {
        List<String> records = new ArrayList<>();

        // Pehle existing lines read karo
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line.trim());
            }
        } catch (IOException ignored) {}

        // Naya value list ke end me add karo (last me append)
        records.add(value);

        // Agar zyada records hain to last 3 hi rakho
        if (records.size() > 3) {
            records = records.subList(records.size() - 3, records.size());
        }

        // Phir file ko overwrite karke updated list write karo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String record : records) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //------------------------------------
    // At the bottom of CredentialUtil.java
    public static String readLatestEntityName(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line, lastLine = null;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
            return lastLine;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String readLatestBankName(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line, lastLine = null;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
            return lastLine;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeToCSV(String filePathUID, String user_Id) {
        List<String> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePathUID))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line.trim());
            }
        } catch (IOException ignored) {}

        records.add(user_Id);

        if (records.size() > 3) {
            records = records.subList(records.size() - 3, records.size()); // keep last 3
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathUID))) {
            for (String record : records) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static List<String> readUserIdsFromCSV(String filePath) {
//        List<String> userIds = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                userIds.add(line.trim());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return userIds;
//    }

//    public static List<String> getUserIdsFromCsv(String filePath) {
//        List<String> userIds = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            boolean isHeader = true;
//            while ((line = br.readLine()) != null) {
//                if (isHeader) {
//                    isHeader = false;
//                    continue;
//                }
//                userIds.add(line.trim());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return userIds;
//    }

    public static String readLastUserIdFromCSV(String filePath) {
        String lastLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lastLine = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastLine;
    }

    public static String generateRandomUTR() {
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000); // 6 digit random number
        return "UTR" + randomNum + System.currentTimeMillis();
    }

    public static void saveLatestToCSV(String filePath,String userFullName, String userId, String password) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) records.add(parts);
            }
        } catch (IOException ignored) {}

        if (records.size() > 2) records = records.subList(records.size() - 2, records.size());

        records.add(new String[]{userFullName, userId, password});

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] readLatestFullDetails(String filePath) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    records.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records.isEmpty() ? null : records.get(records.size() - 1);
    }


}
