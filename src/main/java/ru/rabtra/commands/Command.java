package ru.rabtra.commands;

import lombok.ToString;

public interface Command {

    String execute(String[] args) throws Exception;
    String getName();
    String getDescription();


}
