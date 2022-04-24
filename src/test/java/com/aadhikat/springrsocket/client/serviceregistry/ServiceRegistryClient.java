package com.aadhikat.springrsocket.client.serviceregistry;

//You'll have something similar in Eureka / Consul

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ServiceRegistryClient {

    private List<RSocketServerInstance> instanceList;

    // In production systems, it will go and fetch the instances from the service registry.
    public ServiceRegistryClient() {
        this.instanceList = Arrays.asList(
                new RSocketServerInstance("localhost", 7070),
                new RSocketServerInstance("localhost", 7071),
                new RSocketServerInstance("localhost", 7072)
        );
    }

    // In production systems, public List<RSocketServerInstance> getInstanceList(String name), you'll pass the service name and you'll get the instance list.

    public List<RSocketServerInstance> getInstanceList() {
        return this.instanceList;
    }
}
