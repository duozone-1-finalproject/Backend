package com.example.finalproject.apitest.dto.equity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class TitledGroup<T> {
    private String title;
    private List<T> list;
}