package ru.rabtra.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Item {

    private String name;
    private String path;
    private String type;
    private Long size;

}
