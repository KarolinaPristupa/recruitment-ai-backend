//package com.example.server.controller;
//
//import com.example.server.model.ExternalVacancy;
//import com.example.server.service.HhAuthService;
//import com.example.server.service.HhVacancyService;
//import com.example.server.service.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/hh/vacancies")
//@RequiredArgsConstructor
//public class HhVacancyController {
//
//    private final HhVacancyService hhVacancyService;
//    private final UserService userService;
//    private final HhAuthService hhAuthService;
//
//    @RestController
//    @RequestMapping("/api/hh/vacancies")
//    @RequiredArgsConstructor
//    public class HhVacancyController {
//
//        private final HhVacancyService vacancyService;
//        private final HhAuthService authService;
//        private final UserService userService;
//
//        @PostMapping("/{id}/publish")
//        public ResponseEntity<?> publish(@PathVariable Long id) {
//            var hr = userService.getCurrentUser();
//
//            if (!authService.hasTokenForHr(hr.getId())) {
//                return ResponseEntity.status(401)
//                        .body(authService.getLoginUrl(hr.getId()));
//            }
//
//            return ResponseEntity.ok(vacancyService.publish(id));
//        }
//    }
//
//
//}
//
//
