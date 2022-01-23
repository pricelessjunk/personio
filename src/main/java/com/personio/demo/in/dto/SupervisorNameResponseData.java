package com.personio.demo.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is used to wrap the supervisor name.
 */
@Getter
@Setter
@AllArgsConstructor
public class SupervisorNameResponseData {
    private String supervisor;
    private String level2Supervisor;

    public static SupervisorNameResponseData of(String supervisor, String level2Supervisor) {
        return new SupervisorNameResponseData(supervisor, level2Supervisor);
    }
}
