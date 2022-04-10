package com.aadhikat.springrsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponseDto {

    private int input;
    private int output;

    @Override
    public String toString() {
        return String.format(getFormat(this.output) , this.input , "X");
    }

    private String getFormat (int value) {
        return "%3s|%" + value + "s";
    }
}
