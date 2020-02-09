package com.open.capacity.answer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.answer.dao.AnswerLogsMapper;
import com.open.capacity.answer.dao.QuestionSetMapper;
import com.open.capacity.answer.dao.QuestionStatisticsMapper;
import com.open.capacity.answer.dao.QuestionsMapper;
import com.open.capacity.answer.entity.QuestionSetEntity;
import com.open.capacity.answer.entity.QuestionStatisticsEntity;
import com.open.capacity.answer.entity.QuestionsWithBLOBsEntity;
import com.open.capacity.answer.service.AnswerService;
import com.open.capacity.answer.util.Constants;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.utils.DateUtils;
import com.open.capacity.utils.PageUtil;
import com.open.capacity.utils.SysUserUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private QuestionSetMapper questionSetMapper;
    @Autowired
    private QuestionStatisticsMapper questionStatisticsMapper;
    @Autowired
    private AnswerLogsMapper answerLogsMapper;


    @Override

    public PageResult<QuestionsWithBLOBsEntity> findList(Map<String, Object> params) {
        PageUtil.pageParamConver(params, true);
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        PageHelper.clearPage();
        Integer orgId = SysUserUtil.getLoginOrgId();
        params.put("orgId", orgId);
        List<QuestionsWithBLOBsEntity> list = questionsMapper.findList(params);
        PageInfo<QuestionsWithBLOBsEntity> pageInfo = new PageInfo<>(list);
        return PageResult.<QuestionsWithBLOBsEntity>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public PageResult<QuestionSetEntity> setList(Map<String, Object> params) {
        PageUtil.pageParamConver(params, true);
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        PageHelper.clearPage();
        Integer orgId = SysUserUtil.getLoginOrgId();
        params.put("orgId", orgId);
        List<QuestionSetEntity> list = questionSetMapper.findList(params);
        PageInfo<QuestionSetEntity> pageInfo = new PageInfo<>(list);
        return PageResult.<QuestionSetEntity>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public void saveQuestion(QuestionsWithBLOBsEntity entity) {
        Integer orgId = SysUserUtil.getLoginOrgId();
        entity.setOrgId(orgId);
        questionsMapper.insert(entity);
    }


    /**
     * 添加/更新题目
     *
     * @param entity
     */
    @Override
    public Result saveOrUpdate(QuestionsWithBLOBsEntity entity) {
        String question = entity.getQuestion();
        Integer orgId = SysUserUtil.getLoginOrgId();
        entity.setOrgId(orgId);
        if (StringUtils.isBlank(question)) {
            return Result.failed("问题标题不能为空！");
        }

        if (entity.getQuestionType() == Constants.SELECT_QUESTION
                && (StringUtils.isBlank(entity.getChoicea())
                && StringUtils.isBlank(entity.getChoiceb())
                && StringUtils.isBlank(entity.getChoicec())
                && StringUtils.isBlank(entity.getChoiced())
                && StringUtils.isBlank(entity.getChoicee()))) {// 假如为选择题，必须有选项
            return Result.failed("选择题请输入选项！");
        }
        entity.setUpdateTime(DateUtils.getTimestamp());

        int i = 0;

        if (entity.getId() == null) {
            entity.setCreateTime(DateUtils.getTimestamp());
            i = questionsMapper.insert(entity);
        } else {
            i = questionsMapper.updateByPrimaryKeySelective(entity);
        }
        return i > 0 ? Result.succeed(entity, "操作成功") : Result.failed("操作失败");
    }

    @Override
    public Result bindVideo(QuestionsWithBLOBsEntity entity) {
        Integer orgId = SysUserUtil.getLoginOrgId();
        entity.setOrgId(orgId);
        int i = questionsMapper.updateByPrimaryKeySelective(entity);
        return i > 0 ? Result.succeed(entity, "操作成功") : Result.failed("操作失败");
    }

    @Override
    public Result updateEnabled(Map<String, Object> params) {
        String id = org.apache.commons.collections4.MapUtils.getString(params, "questionLibId");
        Boolean enabled = org.apache.commons.collections4.MapUtils.getBoolean(params, "enabled");

        QuestionSetEntity entity = new QuestionSetEntity();
        entity.setIsAbled(enabled);
        entity.setUpdateTime(DateUtils.getTimestamp());
        entity.setQuestionLibId(id);
        Integer orgId = SysUserUtil.getLoginOrgId();
        entity.setOrgId(orgId);
        int i = questionSetMapper.updateEnabled(entity);

        return i > 0 ? Result.succeed(entity, "更新成功") : Result.failed("更新失败");
    }

    @Override
    public Result queryStatic(String questionTitle, String setId) {
        Integer orgId = SysUserUtil.getLoginOrgId();
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("questionTitle", questionTitle);
        params.put("setId", setId);
        QuestionStatisticsEntity entity = questionStatisticsMapper.selectByOrgId(params);
        if (entity == null) {
            return Result.failed("未找到该题统计信息");
        }
        return Result.succeed(entity, "操作成功");
    }

    @Override
    public Result queryQuestionStatic(String questionLibName) {
        Map<String, Object> params = new HashMap<>();
        Integer orgId = SysUserUtil.getLoginOrgId();
        params.put("orgId", orgId);
        params.put("questionLibName", questionLibName);
        List<QuestionSetEntity> list = questionSetMapper.findListByName(params);
        JSONArray array = new JSONArray();
        JSONObject object;
        JSONObject result = new JSONObject();

        //先查询出来套题
        //再根据套题去查询表中的数据

        for (QuestionSetEntity entity : list) {
            object = new JSONObject();
            params.put("questionId", entity.getQuestionId());
            params.put("setId", entity.getQuestionLibId());
            object.put("name", questionsMapper.selectByPrimaryKey(entity.getQuestionId()).getQuestion());
            object.put("value", answerLogsMapper.selectCount(params));
            array.add(object);
        }
        result.put("datas", array);
        result.put("setId", list.get(0).getQuestionLibId());
        return Result.succeed(JSON.toJSONString(result), "操作成功");
    }
}
