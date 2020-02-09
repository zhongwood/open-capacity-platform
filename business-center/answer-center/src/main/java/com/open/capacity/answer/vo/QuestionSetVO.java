package com.open.capacity.answer.vo;

import com.open.capacity.answer.entity.QuestionSetEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSetVO extends QuestionSetEntity implements Serializable {

    private static final long serialVersionUID = 4132650932311501788L;
    private String question;

    private Integer questionType;

    private String choicea;

    private String choiceb;

    private String choicec;

    private String choiced;

    private String choicee;

    private String videoSerial;

    private String videoSerialTransition;
    private String videoSerialA;
    private String videoSerialB;
    private String videoSerialC;
    private String videoSerialD;
    private String videoSerialE;
    private String videoSerialRight;
    private String videoSerialWrong;

    private String videoSerialURL;
    private String videoSerialTransitionURL;
    private String videoSerialAURL;
    private String videoSerialBURL;
    private String videoSerialCURL;
    private String videoSerialDURL;
    private String videoSerialEURL;
    private String videoSerialRightURL;
    private String videoSerialWrongURL;
    private Integer orgId;
}
