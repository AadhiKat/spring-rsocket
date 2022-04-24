package com.aadhikat.springrsocket.client.serviceregistry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


// In production, we need not include this. This would be coming along with the service registry dependency.

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RSocketServerInstance {

    private String host;
    private int port;

}
