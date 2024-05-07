package vn.unigap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DummyController {
    List<String> list = new ArrayList<>();

    @GetMapping("/dummyapi")
    public String dummyApi() {
        return list.toString();
    }
    @GetMapping("/test/{input}")
    public String test(@PathVariable String input) {
        System.out.print(input);
        list.add(input);
        return "Success " + input;
        //http://localhost:8080/test/use
        //http://localhost:8080/test/add
    }
    @GetMapping("/testParam")
    public String testParam(@RequestParam String inputParam) {
        System.out.print(inputParam);
        return "Done " + inputParam;
        //localhost:8080/testParam?inputParam=testing
    }
    @GetMapping("/testParam2")
    public String testParam(@RequestParam String inputParam, @RequestParam String input2) {
        System.out.print(inputParam + " " + input2);
        return "Done " + inputParam + " " + input2;
        //http://localhost:8080/testParam2?inputParam=test1&input2=test2
    }
    @PostMapping("/products/add")
    public String addProduct(@RequestBody Product p) {
        System.out.print(p);
        return p;
    }
}
