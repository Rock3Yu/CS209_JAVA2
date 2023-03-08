import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Practice4 {

    public record City(String name, String state, int population) {

        @Override
        public String toString() {
            return "City{" +
                    "name='" + name + '\'' +
                    ", state='" + state + '\'' +
                    ", population=" + population +
                    '}';
        }
    }

    public static Stream<City> readCities(String filename) throws IOException {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Stream<City> cities = readCities("cities.txt");
        // Q1: count how many cities there are for each state
        // note: Map<String, Long> cityCountPerState = ...
        Map<String, Long> cityCountPerState = cities.collect(
                Collectors.groupingBy(City::state, Collectors.counting())
        );
        System.out.println(cityCountPerState + "\n");

        cities = readCities("cities.txt");
        // Q2: count the total population for each state
        // note: Map<String, Integer> statePopulation = ...
        Map<String, Integer> statePopulation = cities.collect(
                Collectors.groupingBy(City::state, Collectors.summingInt(City::population))
        );
        System.out.println(statePopulation + "\n");

        cities = readCities("cities.txt");
        // Q3: for each state, get the set of cities with >500,000 population
        // note: Map<String, Set<City>> largeCitiesByState = ...
        Map<String, Set<City>> largeCitiesByState = cities
                .filter(e -> e.population > 500_000)
                .collect(Collectors.groupingBy(City::state,
                                Collectors.mapping(e -> e
//                                        .population() > 500_000 ? e : null
                                        , Collectors.toSet()))
                );
        largeCitiesByState.forEach((key, val) -> System.out.println(key + '=' + val));

    }
}
