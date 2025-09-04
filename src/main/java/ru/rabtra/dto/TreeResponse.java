package ru.rabtra.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TreeResponse {

    private List<Item> items;

}
