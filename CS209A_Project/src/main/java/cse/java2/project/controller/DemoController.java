package cse.java2.project.controller;

import cse.java2.project.Service.AcceptedAnswersService;
import cse.java2.project.Service.NumberOfAnswersService;
import cse.java2.project.Service.TagsAnswersService;
import cse.java2.project.Service.UserAnswersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//
//import static cse.java2.project.Service.UserAnswersService.activeUsers;
//import static cse.java2.project.Service.UserAnswersService.distributionparticipates;

@Controller
public class DemoController {
    AcceptedAnswersService acceptedAnswersService = new AcceptedAnswersService();
    NumberOfAnswersService numberOfAnswersService = new NumberOfAnswersService();
    TagsAnswersService tagsAnswersService = new TagsAnswersService();
    UserAnswersService userAnswersService = new UserAnswersService();

    /**
     * This method is called when the user requests the root URL ("/") or "/demo". In this demo, you
     * can visit localhost:9090 or localhost:9090/demo to see the result.
     *
     * @return the name of the view to be rendered You can find the static HTML file in
     * src/main/resources/templates/demo.html
     */
    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "Index";
    }

    @GetMapping("/1")
    public String Number() {
        numberOfAnswersService.unansweredPercentage("question_2500_upvotes.txt");
        numberOfAnswersService.average_maximumNum("question_2500_upvotes.txt");
        numberOfAnswersService.distributionNum("question_2500_upvotes.txt");
        return "Number";
    }

    @GetMapping("/2")
    public String Accepted() {
        acceptedAnswersService.acceptedPercentage("question_2500_upvotes.txt");
        acceptedAnswersService.distributionResolutionTime("question_2500.txt");
        acceptedAnswersService.moreUpvotesPercentage("question_2500_upvotes.txt");
        return "Accepted";
    }

    @GetMapping("/3")
    public String Tags() {
        tagsAnswersService.distributionTags("question_2500_upvotes.txt");
        tagsAnswersService.distributionUpvotes("question_2500_upvotes.txt");
        tagsAnswersService.distributionViews("question_2500_upvotes.txt");
        return "Tags";
    }

    @GetMapping("/4")
    public String Users() {
        userAnswersService.distributionparticipates("comment_2500_upvotes.txt");
        userAnswersService.activeUsers("comment_2500_upvotes.txt");
        return "Users";
    }

    @GetMapping("/plus1")
    public String JavaAPIs() {
        return "JavaAPIs";
    }
}
