package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IncrDepth {
    private List<List<String>> bids;

    private List<List<String>> asks;

    @Override
    public String toString() {
        return "IncrDepth{" +
                "bids=" + bids +
                ", asks=" + asks +
                '}';
    }
}
