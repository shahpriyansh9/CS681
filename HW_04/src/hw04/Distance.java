package hw04;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Distance {

    public static double get(List<Double> p1, List<Double> p2) {
        return Distance.get(p1, p2, new Euclidean());
    }
    
    public static double get(List<Double> p1, List<Double> p2, DistanceMetric metric) {
        return metric.distance(p1, p2);
    }
    
    public static List<List<Double>> matrix(List<List<Double>> points) {
        return Distance.matrix(points, new Euclidean());
    }
    
    public static List<List<Double>> matrix(List<List<Double>> points, DistanceMetric metric) {
        return points.stream()
                     .map(p1 -> points.stream()
                                      .map(p2 -> metric.distance(p1, p2))
                                      .collect(Collectors.toList()))
                     .collect(Collectors.toList());
    }

    // Main method for testing
    public static void main(String[] args) {
        List<List<Double>> points = generateRandomPoints(1001, 101); // More than 1000 points with more than 100 dimensions
        
        // Calculate and print distances using Euclidean metric
        List<List<Double>> euclideanDistanceMatrix = matrix(points, new Euclidean());
        System.out.println("Euclidean Distance:");
        System.out.println(euclideanDistanceMatrix.get(0).get(1));
        System.out.println(euclideanDistanceMatrix.get(0).get(2));
        
        // Calculate and print distances using Chebyshev metric
        List<List<Double>> chebyshevDistanceMatrix = matrix(points, new Chebyshev());
        System.out.println("Chebyshev Distance:");
        System.out.println(chebyshevDistanceMatrix.get(0).get(1));
        System.out.println(chebyshevDistanceMatrix.get(0).get(2));
        
        // Calculate and print distances using Manhattan metric
        List<List<Double>> manhattanDistanceMatrix = matrix(points, new Manhattan());
        System.out.println("Manhattan Distance:");
        System.out.println(manhattanDistanceMatrix.get(0).get(1));
        System.out.println(manhattanDistanceMatrix.get(0).get(2));
    }

    private static List<List<Double>> generateRandomPoints(int numPoints, int dimensions) {
        Random rand = new Random();
        return IntStream.range(0, numPoints)
                        .mapToObj(i -> IntStream.range(0, dimensions)
                                                .mapToObj(d -> rand.nextDouble() * 100) // Random value between 0 and 100
                                                .collect(Collectors.toList()))
                        .collect(Collectors.toList());
    }
}
