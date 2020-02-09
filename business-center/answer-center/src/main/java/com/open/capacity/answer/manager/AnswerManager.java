package com.open.capacity.answer.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.open.capacity.answer.dao.*;
import com.open.capacity.answer.entity.AnswerLogsEntity;
import com.open.capacity.answer.entity.QuestionSetEntity;
import com.open.capacity.answer.entity.QuestionStatisticsEntity;
import com.open.capacity.answer.entity.QuestionsVideoRelationEntity;
import com.open.capacity.answer.feign.FileClient;
import com.open.capacity.answer.util.Constants;
import com.open.capacity.answer.vo.QuestionSetVO;
import com.open.capacity.answer.vo.QuestionVO;
import com.open.capacity.commons.CodeEnum;
import com.open.capacity.commons.Result;
import com.open.capacity.model.system.VideoInfo;
import com.open.capacity.utils.DateUtils;
import com.open.capacity.utils.SysUserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerManager {

    @Autowired
    private QuestionsMapper questionsMapper;
    @Autowired
    private QuestionSetMapper questionSetMapper;
    @Autowired
    private QuestionsVideoRelationMapper questionsVideoRelationMapper;
    @Autowired
    private AnswerLogsMapper answerLogsMapper;
    @Autowired
    private QuestionStatisticsMapper questionStatisticsMapper;
    @Autowired
    private FileClient fileClient;

    public List<QuestionSetVO> querySetById(Map<String, Object> params) {
        Integer orgId = SysUserUtil.getLoginOrgId();
        params.put("orgId", orgId);
        return questionSetMapper.querySetById(params);
    }

    public Result updateSetLink(QuestionSetEntity entity) {
        Integer orgId = SysUserUtil.getLoginOrgId();
        entity.setOrgId(orgId);
        int resule = questionSetMapper.updateByPrimaryKeySelective(entity);
        return resule == 0 ? Result.failed("更新失败！") : Result.succeed("更新成功！");
    }

    public Result saveSet(List<QuestionSetEntity> list) {

        int resule = questionSetMapper.saveSet(list);
        return resule == 0 ? Result.failed("更新失败！") : Result.succeedWith(list.get(0).getQuestionLibId(), CodeEnum.SUCCESS.getCode(), "更新成功！");
    }

    /**
     * 获取下一题
     *
     * @return
     */
    public Result getVideo(String setId, String questionLibId, Integer orgId) {

        Map<String, Object> params = new HashMap<>();
        params.put("setId", Integer.parseInt(setId));
        params.put("questionLibId", questionLibId);
        QuestionSetVO vo = questionSetMapper.getAbleSetById(params);

        // vo为空
        if (vo == null) {
            return Result.failed("对不起，服务暂时不可用！");
        }
        return findVideo(vo);
    }


    public Result getEndVideo(String questionLibId, Integer orgId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("questionLibId", questionLibId);
        List<QuestionSetVO> list = questionSetMapper.querySetById(params);

        if (CollectionUtils.isEmpty(list)) {
            return Result.failed("没有该题！");
        }
        QuestionsVideoRelationEntity entity = questionsVideoRelationMapper.selectByQuestionId(params);
        if (entity != null) {
            VideoInfo info = fileClient.findByVideoId(entity.getEndVideoSerial(), entity.getOrgId());
            return Result.succeedWith(JSON.toJSONString(info, SerializerFeature.UseSingleQuotes), CodeEnum.SUCCESS.getCode(), null);
        }
        return Result.succeed("无结束视频");
    }

    /**
     * 获取第一题
     *
     * @return
     */
    public Result getFirstVideo(String questionLibId, Integer orgId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("questionLibId", questionLibId);
        List<QuestionSetVO> list = questionSetMapper.querySetById(params);

        if (CollectionUtils.isEmpty(list)) {
            return Result.failed("没有该题！");
        }
        QuestionsVideoRelationEntity entity = questionsVideoRelationMapper.selectByQuestionId(params);
        // 不为空 则根据第一题的id 调用file-center获取关联的视频信息
        QuestionVO questionVO = new QuestionVO();
        VideoInfo info = null;
        if (entity != null) {
            info = fileClient.findByVideoId(entity.getStartVideoSerial(), entity.getOrgId());
            // 无开始视频
            if (info == null) {
                info = fileClient.findByVideoId(list.get(0).getVideoSerial(), orgId);
                QuestionSetVO vo = list.get(0);
//                findVideo(vo);
                setVideoUrl(vo);
                questionVO.setQuestionSetVO(vo);
            } else {
                QuestionSetVO vo = new QuestionSetVO();
                vo.setDefaultNext(String.valueOf(list.get(0).getQuestionId()));
                questionVO.setDefaultNext(String.valueOf(list.get(0).getQuestionId()));
                questionVO.setQuestionSetVO(vo);
            }
        } else {
            info = fileClient.findByVideoId(list.get(0).getVideoSerial(), orgId);
            QuestionSetVO vo = list.get(0);
            setVideoUrl(vo);
            questionVO.setQuestionSetVO(vo);
        }
        questionVO.setVideoInfo(info);
        questionVO.setDefaultNext(String.valueOf(list.get(0).getQuestionId()));
        return Result.succeedWith(JSON.toJSONString(questionVO, SerializerFeature.UseSingleQuotes), CodeEnum.SUCCESS.getCode(), null);
    }

    private Result findVideo(QuestionSetVO vo) {

        if (vo == null) {
            return Result.failed("对不起，服务暂时不可用！");
        }
        // 不为空 则根据第一题的id 调用file-center获取关联的视频信息
        VideoInfo info = fileClient.findByVideoId(vo.getVideoSerial(), vo.getOrgId());
        //封装返回参数
        QuestionVO questionVO = new QuestionVO();
        //题号
        questionVO.setNum("Q" + String.valueOf(vo.getQuestionLevel() + 1));
        if (Constants.SELECT_QUESTION == vo.getQuestionType()) {
            questionVO.setQuestionType(Constants.SELECT_QUESTION);
        } else if (Constants.JUDGE_QUESTION == vo.getQuestionType()) {
            questionVO.setQuestionType(Constants.JUDGE_QUESTION);
        }
        setVideoUrl(vo);
        questionVO.setQuestionSetVO(vo);
        questionVO.setVideoInfo(info);
        questionVO.setDefaultNext(vo.getDefaultNext());

        return Result.succeedWith(JSON.toJSONString(questionVO, SerializerFeature.UseSingleQuotes), CodeEnum.SUCCESS.getCode(), null);
    }


    private void setVideoUrl(QuestionSetVO vo) {
        if (StringUtils.isNotEmpty(vo.getVideoSerial())) {
            vo.setVideoSerialURL(fileClient.findByVideoId(vo.getVideoSerial(), vo.getOrgId()).getPlayURL());
        }
        if (StringUtils.isNotEmpty(vo.getVideoSerialA())) {
            vo.setVideoSerialAURL(fileClient.findByVideoId(vo.getVideoSerialA(), vo.getOrgId()).getPlayURL());
        }
        if (StringUtils.isNotEmpty(vo.getVideoSerialB())) {
            vo.setVideoSerialBURL(fileClient.findByVideoId(vo.getVideoSerialB(), vo.getOrgId()).getPlayURL());
        }
        if (StringUtils.isNotEmpty(vo.getVideoSerialC())) {
            vo.setVideoSerialCURL(fileClient.findByVideoId(vo.getVideoSerialC(), vo.getOrgId()).getPlayURL());
        }
        if (StringUtils.isNotEmpty(vo.getVideoSerialD())) {
            vo.setVideoSerialDURL(fileClient.findByVideoId(vo.getVideoSerialD(), vo.getOrgId()).getPlayURL());
        }
        if (StringUtils.isNotEmpty(vo.getVideoSerialE())) {
            vo.setVideoSerialEURL(fileClient.findByVideoId(vo.getVideoSerialE(), vo.getOrgId()).getPlayURL());
        }
        if (StringUtils.isNotEmpty(vo.getVideoSerialTransition())) {
            vo.setVideoSerialTransitionURL(fileClient.findByVideoId(vo.getVideoSerialTransition(), vo.getOrgId()).getPlayURL());
        }

        if (StringUtils.isNotEmpty(vo.getVideoSerialWrong())) {
            vo.setVideoSerialWrongURL(fileClient.findByVideoId(vo.getVideoSerialWrong(), vo.getOrgId()).getPlayURL());
        }

        if (StringUtils.isNotEmpty(vo.getVideoSerialRight())) {
            vo.setVideoSerialRightURL(fileClient.findByVideoId(vo.getVideoSerialRight(), vo.getOrgId()).getPlayURL());
        }

    }

    /**
     * 保存用户选择
     *
     * @param params
     */
    public void saveUserSelect(Map<String, Object> params) {
        Integer userId = org.apache.commons.collections4.MapUtils.getInteger(params, "userId");
        Integer questionId = org.apache.commons.collections4.MapUtils.getInteger(params, "questionId");
        String answer = org.apache.commons.collections4.MapUtils.getString(params, "answer");
        Integer orgId = org.apache.commons.collections4.MapUtils.getInteger(params, "orgId");
        String setId = org.apache.commons.collections4.MapUtils.getString(params, "setId");

        //更新统计表
        //1 先根据id查询看看有没有数据
        Map<String, Object> param = new HashMap<>();
        param.put("setId", setId);
        param.put("orgId", orgId);
        param.put("questionId", questionId);
        QuestionStatisticsEntity result = questionStatisticsMapper.selectByQuestionId(param);
        QuestionStatisticsEntity questionStatisticsEntity = new QuestionStatisticsEntity();
        boolean opt = false;
        if (result == null) {
            //新增
            opt = true;
        }

        Integer answerCode;
        switch (answer.toUpperCase()) {
            case "A":
                answerCode = Constants.CHOICE_A;
                if (opt) {
                    questionStatisticsEntity.setChoiceaNum(1);
                } else {
                    questionStatisticsEntity.setChoiceaNum(result.getChoiceaNum() + 1);
                }
                break;
            case "B":
                answerCode = Constants.CHOICE_B;
                if (opt) {
                    questionStatisticsEntity.setChoicebNum(1);
                } else {
                    questionStatisticsEntity.setChoicebNum(result.getChoicebNum() + 1);
                }
                break;
            case "C":
                answerCode = Constants.CHOICE_C;
                if (opt) {
                    questionStatisticsEntity.setChoicecNum(1);
                } else {
                    questionStatisticsEntity.setChoicecNum(result.getChoicecNum() + 1);
                }
                break;
            case "D":
                answerCode = Constants.CHOICE_D;
                if (opt) {
                    questionStatisticsEntity.setChoicedNum(1);
                } else {
                    questionStatisticsEntity.setChoicedNum(result.getChoicedNum() + 1);
                }
                break;
            case "E":
                answerCode = Constants.CHOICE_E;
                if (opt) {
                    questionStatisticsEntity.setChoiceeNum(1);
                } else {
                    questionStatisticsEntity.setChoiceeNum(result.getChoiceeNum() + 1);
                }
                break;
            case "R":
                answerCode = Constants.CHOICE_RIGHT;
                if (opt) {
                    questionStatisticsEntity.setRightNum(1);
                } else {
                    questionStatisticsEntity.setRightNum(result.getRightNum() + 1);
                }
                break;
            case "W":
                answerCode = Constants.CHOICE_WRONG;
                if (opt) {
                    questionStatisticsEntity.setWrongNum(1);
                } else {
                    questionStatisticsEntity.setWrongNum(result.getWrongNum() + 1);
                }
                break;
            default:
                answerCode = null;
        }
        if (answerCode == null) {
            return;
        }

        questionStatisticsEntity.setOrgId(orgId);
        questionStatisticsEntity.setQuestionId(questionId);
        questionStatisticsEntity.setUpdateTime(DateUtils.getTimestamp());
        questionStatisticsEntity.setSetId(setId);
        //更新统计表
        if (opt) {
            questionStatisticsEntity.setCreateTime(DateUtils.getTimestamp());
            questionStatisticsMapper.insertSelective(questionStatisticsEntity);
        } else {
            questionStatisticsEntity.setId(result.getId());
            questionStatisticsMapper.updateByPrimaryKeySelective(questionStatisticsEntity);
        }

        //更新用户选择记录表
        AnswerLogsEntity answerLogsEntity = new AnswerLogsEntity();
        answerLogsEntity.setOrgId(orgId);
        answerLogsEntity.setUserId(userId);
        answerLogsEntity.setQuestionId(questionId);
        answerLogsEntity.setAnswer(answerCode);
        answerLogsEntity.setSetId(setId);
        //保存用户选择记录表
        answerLogsMapper.insert(answerLogsEntity);
    }
}
