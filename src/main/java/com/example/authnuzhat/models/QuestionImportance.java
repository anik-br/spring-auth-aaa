package com.example.authnuzhat.models;

import lombok.Getter;

@Getter

public enum QuestionImportance {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    QuestionImportance(int value) {
        this.value = value;
    }

}