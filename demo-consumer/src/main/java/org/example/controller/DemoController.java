package org.example.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.Result;
import org.example.dto.DemoRequestDto;
import org.example.dto.DemoResponseDto;
import org.example.service.DemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DemoController {
    @DubboReference
    private DemoService demoService;
    @ResponseBody
    @RequestMapping("/demo-test")
    public Result<List<DemoResponseDto>> demoTest() {
        DemoRequestDto requestDto = new DemoRequestDto();
        requestDto.setName("testDemo");
        requestDto.setAge(123);
        List<DemoResponseDto> responseDtos = demoService.getDemoList(requestDto);
        return Result.succeed(responseDtos);
    }
}
