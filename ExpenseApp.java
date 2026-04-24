import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseApp {

    private static final String FILE_NAME = "expenses.json";

    // CREATE
    public static void createExpense(String description, double amount) {

        List<String> lines = new ArrayList<>();

        try {

            File file = new File(FILE_NAME);

            int id = getNextId();

            String newExpense =
                    "  {\n" +
                    "    \"id\": " + id + ",\n" +
                    "    \"description\": \"" + description + "\",\n" +
                    "    \"amount\": " + amount + "\n" +
                    "  }";

            if (file.exists()) {

                BufferedReader reader =
                        new BufferedReader(new FileReader(file));

                String line;

                while ((line = reader.readLine()) != null) {
                    lines.add(line.trim());
                }

                reader.close();

                if (!lines.isEmpty()
                        && lines.get(lines.size() - 1).equals("]")) {

                    lines.remove(lines.size() - 1);
                }

            } else {

                lines.add("[");
            }

            if (lines.size() > 1) {

                String last = lines.get(lines.size() - 1);

                if (!last.equals("[")) {
                    lines.set(lines.size() - 1, last + ",");
                }
            }

            lines.add(newExpense);

            lines.add("]");

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(FILE_NAME));

            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }

            writer.close();

            System.out.println("Expense saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving expense: "
                    + e.getMessage());
        }
    }



    // VIEW
    public static void viewExpenses() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No expenses found");
            return;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            System.out.println("Current Expenses:");
            System.out.println("-----------------");

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("\"id\"")) {

                    String id =
                            line.split(":")[1]
                                    .replace(",", "")
                                    .trim();

                    System.out.println("ID: " + id);
                }

                if (line.contains("\"description\"")) {

                    String description =
                            line.split(":")[1]
                                    .replace("\"", "")
                                    .replace(",", "")
                                    .trim();

                    System.out.println("Description: "
                            + description);
                }

                if (line.contains("\"amount\"")) {

                    String amount =
                            line.split(":")[1]
                                    .trim();

                    System.out.println("Amount: "
                            + amount);

                    System.out.println();
                }
            }

            reader.close();

        } catch (Exception e) {

            System.out.println(
                    "Error reading expenses: "
                            + e.getMessage());
        }
    }



    // UPDATE
    public static void updateExpense(
            int targetId,
            String newDescription,
            double newAmount) {

        List<String> lines = new ArrayList<>();

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No expenses found.");
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("\"id\"")) {

                    int currentId =
                            Integer.parseInt(
                                    line.split(":")[1]
                                            .replace(",", "")
                                            .trim()
                            );

                    if (currentId == targetId) {

                        found = true;

                        lines.add(line);

                        lines.add(
                                "\"description\": \""
                                        + newDescription + "\","
                        );

                        lines.add(
                                "\"amount\": "
                                        + newAmount
                        );

                        reader.readLine();
                        reader.readLine();

                        continue;
                    }
                }

                lines.add(line);
            }

            reader.close();

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(FILE_NAME)
                    );

            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }

            writer.close();

            if (found) {

                System.out.println(
                        "Expense updated successfully."
                );

            } else {

                System.out.println(
                        "Expense with ID "
                                + targetId
                                + " not found."
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error updating expense: "
                            + e.getMessage()
            );
        }
    }



    // DELETE
    public static void deleteExpense(int targetId) {

        List<String> lines = new ArrayList<>();

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No expenses found.");
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("\"id\"")) {

                    int currentId =
                            Integer.parseInt(
                                    line.split(":")[1]
                                            .replace(",", "")
                                            .trim()
                            );

                    if (currentId == targetId) {

                        found = true;

                        reader.readLine();
                        reader.readLine();
                        reader.readLine();

                        continue;
                    }
                }

                lines.add(line);
            }

            reader.close();

            // Fix trailing comma
            for (int i = lines.size() - 1; i >= 0; i--) {

                if (lines.get(i).endsWith(",")) {

                    lines.set(
                            i,
                            lines.get(i).substring(
                                    0,
                                    lines.get(i).length() - 1
                            )
                    );

                    break;
                }
            }

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(FILE_NAME)
                    );

            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }

            writer.close();

            if (found) {

                System.out.println(
                        "Expense deleted successfully."
                );

            } else {

                System.out.println(
                        "Expense with ID "
                                + targetId
                                + " not found."
            );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error deleting expense: "
                            + e.getMessage()
            );
        }
    }



    // SUMMARY (TOTAL ONLY)
    public static void showSummary() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No expenses found.");
            return;
        }

        double total = 0;

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.contains("\"amount\"")) {

                    double amount =
                            Double.parseDouble(
                                    line.split(":")[1]
                                            .trim()
                            );

                    total += amount;
                }
            }

            reader.close();

            System.out.println(
                    "Total expenses so far: "
                            + total
            );

        } catch (IOException e) {

            System.out.println(
                    "Error calculating summary: "
                            + e.getMessage());
        }
    }



    // PRICE FILTER
    public static void filterByPrice(double minAmount) {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No expenses found.");
            return;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            int currentId = 0;
            String description = "";
            double amount = 0;

            System.out.println(
                    "Filtered Expenses (>= "
                            + minAmount + ")"
            );

            System.out.println("-----------------------");

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("\"id\"")) {

                    currentId =
                            Integer.parseInt(
                                    line.split(":")[1]
                                            .replace(",", "")
                                            .trim()
                            );
                }

                if (line.contains("\"description\"")) {

                    description =
                            line.split(":")[1]
                                    .replace("\"", "")
                                    .replace(",", "")
                                    .trim();
                }

                if (line.contains("\"amount\"")) {

                    amount =
                            Double.parseDouble(
                                    line.split(":")[1]
                                            .trim()
                            );

                    if (amount >= minAmount) {

                        System.out.println(
                                "ID: " + currentId);

                        System.out.println(
                                "Description: "
                                        + description);

                        System.out.println(
                                "Amount: " + amount);

                        System.out.println();
                    }
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error filtering expenses: "
                            + e.getMessage());
        }
    }



    // ID GENERATOR
    public static int getNextId() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return 1;
        }

        int lastId = 0;

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("\"id\"")) {

                    int id =
                            Integer.parseInt(
                                    line.split(":")[1]
                                            .replace(",", "")
                                            .trim()
                            );

                    if (id > lastId) {
                        lastId = id;
                    }
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading ID: "
                            + e.getMessage());
        }

        return lastId + 1;
    }



    public static void main(String[] args) {

        if (args.length == 0) {

            System.out.println("Usage:");
            System.out.println("Add Item");
            System.out.println(" java ExpenseApp <description> <amount>");

            System.out.println("View Expenses");
            System.out.println(" java ExpenseApp view");

            System.out.println("Update Item");
            System.out.println(" java ExpenseApp update <id> <description> <amount>");

            System.out.println("Delete Item");
            System.out.println(" java ExpenseApp delete <id>");

            System.out.println("View Summary");
            System.out.println(" java ExpenseApp summary");
            System.out.println(" java ExpenseApp price <amount>");

            return;
        }

        if (args[0].equalsIgnoreCase("view")) {
            viewExpenses();
            return;
        }

        if (args[0].equalsIgnoreCase("summary")) {
            showSummary();
            return;
        }

        if (args[0].equalsIgnoreCase("price")) {

            double amount =
                    Double.parseDouble(args[1]);

            filterByPrice(amount);
            return;
        }

        if (args[0].equalsIgnoreCase("update")) {

            int id =
                    Integer.parseInt(args[1]);

            String description =
                    args[2];

            double amount =
                    Double.parseDouble(args[3]);

            updateExpense(
                    id,
                    description,
                    amount
            );

            return;
        }

        if (args[0].equalsIgnoreCase("delete")) {

            int id =
                    Integer.parseInt(args[1]);

            deleteExpense(id);
            return;
        }

        String description = args[0];

        double amount =
                Double.parseDouble(args[1]);

        createExpense(
                description,
                amount
        );
    }
}