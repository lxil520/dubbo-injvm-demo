package org.example.service;

import org.example.dto.DemoRequestDto;
import org.example.dto.DemoResponseDto;

import java.util.List;

public interface DemoService {
    public List<DemoResponseDto> getDemoList(DemoRequestDto requestDto);
}
