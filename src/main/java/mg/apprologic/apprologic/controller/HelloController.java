package mg.apprologic.apprologic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/")
    public String getIndex()
    {
        return "index";
    }
    @GetMapping("/getTest")
    public String getTest()
    {
        return "test";
    }
}
