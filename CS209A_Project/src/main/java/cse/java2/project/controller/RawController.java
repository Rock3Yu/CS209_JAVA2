package cse.java2.project.controller;


import cse.java2.project.RestAPIs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RawController {

    @GetMapping("/QuestionDistribution")
    public String questionDistribution(@RequestParam(value = "num", defaultValue = "-1") int num) {
        return RestAPIs.QuestionDistribution(num);
    }

    @GetMapping("/AcceptedTimeDistribution")
    public String acceptedTimeDistribution(
            @RequestParam(value = "num", defaultValue = "-1") int num) {
        return RestAPIs.AcceptedTimeDistribution(num);
    }

    @GetMapping("/MostActiveUsersId")
    public String mostActiveUsersId(@RequestParam(value = "min", defaultValue = "20") int min) {
        return RestAPIs.MostActiveUsersId(min);
    }
}
