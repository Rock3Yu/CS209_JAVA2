import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;
import lombok.Getter;
import lombok.Setter;  // NOTE: ATTENTION! YOU SHOULD USE "EXPLICIT GETTER AND SETTER" TO PASS "OJ"


/**
 * This is just a demo for you, please run it on JDK17 (some statements may be not allowed in lower
 * version). This is just a demo, and you can extend and implement functions based on this demo, or
 * implement it in a different way.
 */
@SuppressWarnings({"checkstyle:Indentation", "checkstyle:MissingJavadocMethod"})
public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();

    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4],
                        info[5],
                        Integer.parseInt(info[6]), Integer.parseInt(info[7]),
                        Integer.parseInt(info[8]),
                        Integer.parseInt(info[9]), Integer.parseInt(info[10]),
                        Double.parseDouble(info[11]),
                        Double.parseDouble(info[12]), Double.parseDouble(info[13]),
                        Double.parseDouble(info[14]),
                        Double.parseDouble(info[15]), Double.parseDouble(info[16]),
                        Double.parseDouble(info[17]),
                        Double.parseDouble(info[18]), Double.parseDouble(info[19]),
                        Double.parseDouble(info[20]),
                        Double.parseDouble(info[21]), Double.parseDouble(info[22]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    public Map<String, Integer> getPtcpCountByInst() {
        return courses.stream().collect(Collectors.groupingBy(
                Course::getInstitution, Collectors.summingInt(Course::getParticipants)));
    }

    //2
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        Stream<Course> stream = courses.stream();
        Map<String, Integer> res = stream.collect(Collectors.groupingBy(
                e -> e.institution + "-" + e.subject,
                Collectors.summingInt(Course::getParticipants)
        ));
        return res.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum,
                        LinkedHashMap::new)
                );
    }

    //3
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String, List<List<String>>> map = new HashMap<>();
        courses.forEach(e -> {
            String[] instructors = e.getInstructors().split(", ");
            for (String instructor : instructors) {
                if (!map.containsKey(instructor)) {
                    List<List<String>> lls = new ArrayList<>();
                    lls.add(new ArrayList<>());
                    lls.add(new ArrayList<>());
                    map.put(instructor, lls);
                }
                if (instructors.length == 1) {
                    map.get(instructors[0]).get(0).add(e.getTitle());
                } else {
                    map.get(instructor).get(1).add((e.getTitle()));
                }
            }
        });
        map.forEach((key, value) -> {
            value.add(0, value.remove(0).stream().distinct().sorted().toList());
            value.add(1, value.remove(1).stream().distinct().sorted().toList());
        });
        return map;
    }

    //4
    public List<String> getCourses(int topK, String by) {
        Stream<Course> stream = courses.stream();
        if (by.equals("hours")) {
            return stream
                    .sorted(Comparator.comparing(Course::getSubject))
                    .sorted(Comparator.comparingDouble(Course::getTotalHours).reversed())
                    .map(Course::getTitle)
                    .distinct()
                    .limit(topK)
                    .toList();
        } else if (by.equals("participants")) {
            return stream
                    .sorted(Comparator.comparing(Course::getSubject))
                    .sorted(Comparator.comparingInt(Course::getParticipants).reversed())
                    .map(Course::getTitle)
                    .distinct()
                    .limit(topK)
                    .toList();
        }
        return null;
    }

    //5
    public List<String> searchCourses(String courseSubject, double percentAudited,
            double totalCourseHours) {
        return courses.stream()
                .filter(e -> e.getSubject().toLowerCase().contains(courseSubject.toLowerCase()))
                .filter(e -> e.getPercentAudited() >= percentAudited)
                .filter(e -> e.getTotalHours() <= totalCourseHours)
                .sorted(Comparator.comparing(Course::getTitle))
                .map(Course::getTitle)
                .distinct()
                .toList();
    }

    //6
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        Map<String, double[]> map = new HashMap<>();
        Map<String, String> num2title = new HashMap<>();
        List.copyOf(courses).stream()
                .sorted(Comparator.comparing(Course::getLaunchDate).reversed())
                .forEach(e -> {
                    if (map.containsKey(e.getNumber())) {
                        double[] arr = map.get(e.getNumber());
                        arr[0]++;
                        arr[1] += e.getMedianAge();
                        arr[2] += e.getPercentMale();
                        arr[3] += e.getPercentDegree();
                        map.replace(e.getNumber(), arr);
                    } else {
                        double[] arr = {1,
                                e.getMedianAge(),
                                e.getPercentMale(),
                                e.getPercentDegree()};
                        map.put(e.getNumber(), arr);
                        num2title.put(e.getNumber(), e.getTitle());
                    }
                });

        return map.entrySet().stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .sorted(Comparator.comparingDouble(e -> {
                    double[] arr = e.getValue();
                    return similarity(
                            age, arr[1] / arr[0],
                            gender * 100, arr[2] / arr[0],
                            isBachelorOrHigher * 100, arr[3] / arr[0]
                    );
                }))
                .map(e -> num2title.get(e.getKey()))
                .distinct()
                .limit(10)
                .toList();
    }

    private double similarity(double a1, double a2, double b1, double b2, double c1, double c2) {
        return Math.pow((a1 - a2), 2) + Math.pow((b1 - b2), 2) + Math.pow((c1 - c2), 2);
    }

}

@Getter
@Setter
@SuppressWarnings({"checkstyle:Indentation", "checkstyle:MissingJavadocMethod",
        "checkstyle:OneTopLevelClass"})
class Course {

    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;

    public Course(String institution, String number, Date launchDate,
            String title, String instructors, String subject,
            int year, int honorCode, int participants,
            int audited, int certified, double percentAudited,
            double percentCertified, double percentCertified50,
            double percentVideo, double percentForum, double gradeHigherZero,
            double totalHours, double medianHoursCertification,
            double medianAge, double percentMale, double percentFemale,
            double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) {
            title = title.substring(1);
        }
        if (title.endsWith("\"")) {
            title = title.substring(0, title.length() - 1);
        }
        this.title = title;
        if (instructors.startsWith("\"")) {
            instructors = instructors.substring(1);
        }
        if (instructors.endsWith("\"")) {
            instructors = instructors.substring(0, instructors.length() - 1);
        }
        this.instructors = instructors;
        if (subject.startsWith("\"")) {
            subject = subject.substring(1);
        }
        if (subject.endsWith("\"")) {
            subject = subject.substring(0, subject.length() - 1);
        }
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }
}