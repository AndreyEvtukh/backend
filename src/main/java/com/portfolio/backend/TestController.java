package com.portfolio.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public Map<String, Object> test() {
        return Map.of(
                "data", "Hello Oracle!",
                "value", 2,
                "status", 200,
                "ok", true
        );
    }
}
