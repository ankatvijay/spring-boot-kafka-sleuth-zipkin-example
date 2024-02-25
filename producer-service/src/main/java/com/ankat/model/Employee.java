package com.ankat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.StringJoiner;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {

    @JsonProperty(value = "emp-id")
    private Integer employeeId;

    @JsonProperty(value = "emp-name")
    private String employeeName;

    @JsonProperty(value = "emp-address")
    private String employeeAddress;

    @Override
    public String toString() {
        return new StringJoiner(", ", "{", "}")
                .add(String.format("\"employeeId\":%d", employeeId))
                .add(String.format("\"employeeName\":\"%s\"", employeeName))
                .add(String.format("\"employeeAddress\":\"%s\"", employeeAddress))
                .toString();
    }
}
