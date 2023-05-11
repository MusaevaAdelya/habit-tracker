package com.example.habittracker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmptyFileException extends  RuntimeException{
    private final String msg;
}