package com.reactive.webfluxdemo.controller;

import com.reactive.webfluxdemo.dto.Response;
import com.reactive.webfluxdemo.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("math")
public class MathController {

    @Autowired
    private MathService mathService;

    @GetMapping("square/{input}")
    public Response findSquare(@PathVariable int input) {
        return mathService.findSquare(input);
    }

    @GetMapping("table/{input}")
    public List<Response> multiplicationTable(@PathVariable int input) {
        return mathService.multiplicationTable(input);
    }

}
