package hw18;

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
        Path path = Paths.get("EmployeeEarning.csv");

        try (Stream<String> lines = Files.lines(path)) {
            List<List<String>> data = lines
                    .skip(1)
                    .map(line -> SPLIT_PATTERN.splitAsStream(line)
                            .map(value -> value.replaceAll("\"", "").trim())
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            List<Double> totalEarnings = data.parallelStream() // Changed to parallelStream
                    .map(row -> parseDoubleSafely(row.get(row.size() - 2))) // Index for TOTAL_GROSS
                    .filter(OptionalDouble::isPresent)
                    .mapToDouble(OptionalDouble::getAsDouble)
                    .boxed()
                    .collect(Collectors.toList());

            double maxEarnings = totalEarnings.parallelStream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
            double minEarnings = totalEarnings.parallelStream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
            double avgEarnings = totalEarnings.parallelStream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

            System.out.println("Maximum earnings: " + maxEarnings);
            System.out.println("Minimum earnings: " + minEarnings);
            System.out.println("Average earnings: " + avgEarnings);

            List<List<String>> sortedByOvertime = data.parallelStream() // Changed to parallelStream
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
            });

            List<List<String>> bostonPoliceOvertime = data.parallelStream() // Changed to parallelStream
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

        } catch (IOException ex) {
            System.out.println("An exception occurred while processing the file: " + ex.getMessage());
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
