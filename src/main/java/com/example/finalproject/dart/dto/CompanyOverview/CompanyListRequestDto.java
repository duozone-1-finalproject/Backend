package com.example.finalproject.dart.dto.CompanyOverview;

import com.example.finalproject.dart.entity.CompanyOverview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListRequestDto {
    private List<CompanyOverview> companies;
}
