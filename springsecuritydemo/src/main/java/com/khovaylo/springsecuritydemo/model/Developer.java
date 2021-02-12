package com.khovaylo.springsecuritydemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Pavel Khovaylo
 */
@Data
@AllArgsConstructor
public class Developer {
    private Long id;
    private String firstName;
    private String lastName;
}
