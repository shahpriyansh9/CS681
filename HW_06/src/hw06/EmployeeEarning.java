package hw06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeEarning {

    private static final Pattern SPLIT_PATTERN = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

    public static void main(String[] args) {
        List<List<String>> data = loadData("EmployeeEarning.csv");

        // Create Runnables for each data processing task
        Runnable maxEarningsTask = () -> processMaxEarnings(data);
        Runnable highestOvertimeTask = () -> processHighestOvertime(data);
        Runnable bostonPoliceOvertimeTask = () -> processBostonPoliceOvertime(data);

        // Start each task in a new thread
        Thread maxEarningsThread = new Thread(maxEarningsTask);
        Thread highestOvertimeThread = new Thread(highestOvertimeTask);
        Thread bostonPoliceOvertimeThread = new Thread(bostonPoliceOvertimeTask);

        maxEarningsThread.start();
        highestOvertimeThread.start();
        bostonPoliceOvertimeThread.start();

        // Wait for all threads to complete
        try {
            maxEarningsThread.join();
            highestOvertimeThread.join();
            bostonPoliceOvertimeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All data processing tasks completed.");
    }

    private static void processMaxEarnings(List<List<String>> data) {
        List<Double> totalEarnings = data.stream()
                .map(row -> parseDoubleSafely(row.get(row.size() - 2)))
                .filter(OptionalDouble::isPresent)
                .mapToDouble(OptionalDouble::getAsDouble)
                .boxed()
                .collect(Collectors.toList());

        
                double maxEarnings = totalEarnings.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
                double minEarnings = totalEarnings.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
                double avgEarnings = totalEarnings.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    
                System.out.println("Maximum earnings: " + maxEarnings);
                System.out.println("Minimum earnings: " + minEarnings);
                System.out.println("Average earnings: " + avgEarnings);
    }

    private static void processHighestOvertime(List<List<String>> data) {
        List<List<String>> sortedByOvertime = data.stream()
        .filter(row -> parseDoubleSafely(row.get(6)).isPresent())
        .sorted((row1, row2) -> {
            OptionalDouble overtime1 = parseDoubleSafely(row1.get(6));
            OptionalDouble overtime2 = parseDoubleSafely(row2.get(6));
            return Double.compare(overtime2.orElse(0.0), overtime1.orElse(0.0)); 
        })
        .collect(Collectors.toList());

int numHighestOvertimeRows = (int) Math.ceil(data.size() * 0.1);
List<List<String>> highestOvertimeRows = sortedByOvertime.subList(0, numHighestOvertimeRows);

System.out.println("\nTop 10% employees with highest overtime earnings:");
highestOvertimeRows.forEach(row -> {
    String name = row.get(0);
    String department = row.get(1);
    String overtime = row.get(6); 
    System.out.println("[" + name + ", " + department + ", " + overtime + "]");
});    }

    private static void processBostonPoliceOvertime(List<List<String>> data) {
        List<List<String>> bostonPoliceOvertime = data.stream()
        .filter(row -> row.get(1).contains("Police")) // Filtering for Boston Police Department based on DEPARTMENT_NAME
        .sorted(Comparator.comparingDouble((List<String> row) ->
            parseDoubleSafely(row.get(6)).orElse(0.0)).reversed()) // Sort by OVERTIME 
        .limit(10) 
        .collect(Collectors.toList());
    
    System.out.println("\nTop 10 highest overtime incomes for Boston Police Department:");
    bostonPoliceOvertime.forEach(row -> {
        String name = row.get(0);
        String department = row.get(1);
        String title = row.get(2);
        String overtime = row.get(6); 
        System.out.println(name + ", " + department + ", " + title + ", " + overtime);
    });    
}

    private static List<List<String>> loadData(String fileName) {
        Path path = Paths.get(fileName);
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .map(line -> SPLIT_PATTERN.splitAsStream(line)
                            .map(value -> value.replaceAll("\"", "").trim())
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            System.out.println("An exception occurred while processing the file: " + ex.getMessage());
            return null;
        }
    }

    private static OptionalDouble parseDoubleSafely(String str) {
        try {
            String number = str.replaceAll(",", "");
            return OptionalDouble.of(Double.parseDouble(number));
        } catch (NumberFormatException e) {
            return OptionalDouble.empty();
        }
    }
}
