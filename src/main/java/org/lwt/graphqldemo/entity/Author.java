package org.lwt.graphqldemo.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Author {

	private Integer id;
    private String name;
    private Integer age;

}