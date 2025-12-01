package com.example.server.mock;

import com.example.server.dto.request.HhVacancyCreateRequest;
import com.example.server.dto.response.HhVacancyCreateResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock-hh")
public class HhVacancyMockController {

    @PostMapping("/vacancies")
    public HhVacancyCreateResponse create(@RequestBody HhVacancyCreateRequest req) {
        HhVacancyCreateResponse r = new HhVacancyCreateResponse();
        r.setId("mock-12345");
        r.setUrl("http://mock-hh/vacancies/mock-12345");
        return r;
    }
}

