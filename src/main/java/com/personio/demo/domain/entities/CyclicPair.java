package com.personio.demo.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CyclicPair {
    private String supervisor;
    private String subordinate;

    @Override
    public String toString() {
        return subordinate + " has a child '" + subordinate + "' who is also a supervisor";
    }
}
