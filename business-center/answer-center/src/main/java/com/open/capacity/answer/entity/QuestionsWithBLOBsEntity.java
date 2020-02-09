package com.open.capacity.answer.entity;

import lombok.Data;

@Data
public class QuestionsWithBLOBsEntity extends QuestionsEntity {
    private String question;

    private String choicea;

    private String choiceb;

    private String choicec;

    private String choiced;

    private String choicee;
    private String videoSerialTransition;
    private String videoSerialA;
    private String videoSerialB;
    private String videoSerialC;
    private String videoSerialD;
    private String videoSerialE;
    private String videoSerialRight;
    private String videoSerialWrong;
    private Integer orgId;


}