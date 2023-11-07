package hw02;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

enum AgeCat {
    OLD, MID, YOUNG
}

class Person {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private List<Dose> doses = new LinkedList<>();

    public Person(String firstName, String lastName, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    public int getAge() {
        return LocalDate.now().getYear() - dob.getYear();
    }

    public AgeCat getAgeCat() {
        if (getAge() >= 50) {
            return AgeCat.OLD;
        } else if (getAge() >= 18) {
            return AgeCat.MID;
        } else {
            return AgeCat.YOUNG;
        }
    }

    public void addDose(Dose dose) {
        doses.add(dose);
    }

    public int getVacCount() {
        return doses.size();
    }

    public boolean isFullyVaccinated() {
        return getAge() >= 18 && getVacCount() >= 3;
    }
}

class Dose {
    private String vacName;
    private String lotNumber;
    private LocalDate date;
    private String vacSite;

    public Dose(String vacName, String lotNumber, LocalDate date, String site) {
        this.vacName = vacName;
        this.lotNumber = lotNumber;
        this.date = date;
        this.vacSite = site;
    }
}

public class Vaccination {
    public static void main(String[] args) {
        List<Person> people = generateRandomPeople(1000);

        Map<AgeCat, Double> vacRatesByAge = calculateVacRatesByAge(people);
        Map<AgeCat, Double> avgDosesByAge = calculateAvgDosesByAge(people);
        double avgAgeUnvac = calculateAvgAgeUnvac(people);

        System.out.println("Vaccination rates by age category: " + vacRatesByAge);
        System.out.println("Average number of doses by age category: " + avgDosesByAge);
        System.out.println("Average age of unvaccinated people: " + avgAgeUnvac);
    }

    private static List<Person> generateRandomPeople(int count) {
        Random rand = new Random();
        List<Person> people = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            int age = rand.nextInt(100);
            Person p = new Person("Fname" + i, "Lname" + i, LocalDate.now().minusYears(age));
            int numDoses = rand.nextInt(5);
            for (int j = 0; j < numDoses; j++) {
                p.addDose(new Dose("Vaccine" + j, "Lot" + j, LocalDate.now().minusMonths(j), "Site" + j));
            }
            people.add(p);
        }
        return people;
    }

    private static Map<AgeCat, Double> calculateVacRatesByAge(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getAgeCat,
                        Collectors.averagingDouble(p -> p.isFullyVaccinated() ? 1.0 : 0.0)));
    }

    private static Map<AgeCat, Double> calculateAvgDosesByAge(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getAgeCat,
                        Collectors.averagingDouble(Person::getVacCount)));
    }

    private static double calculateAvgAgeUnvac(List<Person> people) {
        return people.stream()
                .filter(p -> !p.isFullyVaccinated())
                .collect(Collectors.averagingDouble(Person::getAge));
    }
}
