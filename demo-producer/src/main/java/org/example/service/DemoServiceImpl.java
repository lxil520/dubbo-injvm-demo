package org.example.service;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.DemoRequestDto;
import org.example.dto.DemoResponseDto;

import java.util.ArrayList;
import java.util.List;
@DubboService
public class DemoServiceImpl implements DemoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public List<DemoResponseDto> getDemoList(DemoRequestDto requestDto) {
        LOGGER.info("demo request name=" +requestDto.getName() +", age="+ requestDto.getAge());
        List<DemoResponseDto> responseDtoList = new ArrayList<>();
        DemoResponseDto responseDtoFirst = new DemoResponseDto();
        responseDtoFirst.setCode("100");
        responseDtoFirst.setName("testDemo1");
        responseDtoFirst.setAge(1);
        responseDtoFirst.setAddress("city");

        DemoResponseDto responseDtoSecond = new DemoResponseDto();
        responseDtoSecond.setCode("200");
        responseDtoSecond.setName("testDemo2");
        responseDtoSecond.setAge(2);
        responseDtoSecond.setAddress("province");

        responseDtoList.add(responseDtoFirst);
        responseDtoList.add(responseDtoSecond);
        return responseDtoList;
    }
}
