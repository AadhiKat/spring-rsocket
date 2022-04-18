package com.aadhikat.springrsocket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
public class ClientConnectionRequest {

    private String clientId;
    private String secretKey;

}
