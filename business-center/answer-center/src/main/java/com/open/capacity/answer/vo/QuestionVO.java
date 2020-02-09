package com.open.capacity.answer.vo;

import com.open.capacity.model.system.VideoInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionVO implements Serializable {
    private static final long serialVersionUID = 9031947836127177876L;

    private String num;

    private Integer questionType;

    private QuestionSetVO questionSetVO;

    private String defaultNext;

    private VideoInfo videoInfo;


}
