package hw04;

import java.util.List;
import java.util.stream.IntStream;

public class Manhattan implements DistanceMetric {

    @Override
    public double distance(List<Double> p1, List<Double> p2) {
        if (p1.size() != p2.size()) {
            throw new IllegalArgumentException("The sizes of the two lists must be the same.");
        }

        double sumOfAbsolute = IntStream.range(0, p1.size())
                .mapToDouble(i -> Math.abs(p1.get(i) - p2.get(i)))
                .sum();

        return sumOfAbsolute;
    }
}
