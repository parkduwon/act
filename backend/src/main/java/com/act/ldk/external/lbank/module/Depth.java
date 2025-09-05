package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Depth {

    private List<List<Double>> bids;

    private List<List<Double>> asks;

    private Long version;

    private Long timestamp;

    @Override
    public String toString() {
        return "Depth{" +
                "bids=" + bids +
                ", asks=" + asks +
                ", version=" + version +
                ", timestamp=" + timestamp +
                '}';
    }
}
