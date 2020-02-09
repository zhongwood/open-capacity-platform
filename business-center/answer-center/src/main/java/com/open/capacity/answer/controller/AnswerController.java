package com.open.capacity.answer.controller;

import com.alibaba.fastjson.JSON;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.answer.dao.QuestionSetMapper;
import com.open.capacity.answer.dao.QuestionsMapper;
import com.open.capacity.answer.dao.QuestionsVideoRelationMapper;
import com.open.capacity.answer.entity.QuestionSetEntity;
import com.open.capacity.answer.entity.QuestionsVideoRelationEntity;
import com.open.capacity.answer.entity.QuestionsWithBLOBsEntity;
import com.open.capacity.answer.manager.AnswerManager;
import com.open.capacity.answer.service.AnswerService;
import com.open.capacity.answer.vo.QuestionSetVO;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.utils.DateUtils;
import com.open.capacity.utils.SysUserUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AnswerController {

    private static Logger log = LoggerFactory.getLogger(AnswerController.class);

    @Autowired
    private AnswerManager answerManager;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private QuestionSetMapper questionSetMapper;

    @Autowired
    private QuestionsVideoRelationMapper questionsVideoRelationMapper;

    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:query')")
    @GetMapping("/list")
    public PageResult<QuestionsWithBLOBsEntity> findList(@RequestParam Map<String, Object> params) {

        return answerService.findList(params);

    }

    /**
     * 获取列表
     *
     * @return
     */
    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:query')")
    @GetMapping("/questionList")
    public List<QuestionsWithBLOBsEntity> questionList() {
        return questionsMapper.findList(null);

    }

    /**
     * 新增or更新
     *
     * @param entity
     * @return
     */
    @PostMapping("/answer/saveOrUpdate")
    @PreAuthorize("hasAnyAuthority('answer:post/answer/saveOrUpdate')")
    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody QuestionsWithBLOBsEntity entity) {
        return answerService.saveOrUpdate(entity);
    }

    /**
     * 关联视频
     *
     * @param params
     * @return
     * @throws Exception
     */
    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:post/answer/saveOrUpdate')")
    @PostMapping("/bindVideo")
    public Result bindVideo(@RequestBody Map<String, Object> params) throws Exception {

        QuestionsWithBLOBsEntity entity = new QuestionsWithBLOBsEntity();

        entity.setVideoSerial((String) params.get("videoId"));
        entity.setVideoSerialTransition((String) params.get("videoIdTransition"));

        if (1 == (Integer) (params.get("questionType"))) {
            entity.setVideoSerialA((String) params.get("videoIdA"));
            entity.setVideoSerialB((String) params.get("videoIdB"));
            entity.setVideoSerialC((String) params.get("videoIdC"));
            entity.setVideoSerialD((String) params.get("videoIdD"));
            entity.setVideoSerialE((String) params.get("videoIdE"));
        } else if (2 == (Integer) (params.get("questionType"))) {
            entity.setVideoSerialRight((String) params.get("videoIdRight"));
            entity.setVideoSerialWrong((String) params.get("videoIdWrong"));
        }


        entity.setId((Integer) params.get("id"));
        entity.setUpdateTime(DateUtils.getTimestamp());

        return answerService.bindVideo(entity);
    }


    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:query')")
    @GetMapping("/setList")
    public PageResult<QuestionSetEntity> setList(@RequestParam Map<String, Object> params) {

        return answerService.setList(params);

    }

    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:query')")
    @GetMapping("/querySetById")
    public List<QuestionSetVO> querySetById(@RequestParam Map<String, Object> params) {

        return answerManager.querySetById(params);

    }

    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:post/answer/saveOrUpdate')")
    @PostMapping("/updateSetLink")
    public Result updateSetLink(@RequestBody Map<String, Object> params) {

        Integer id = Integer.parseInt((String) params.get("id"));
        String choice = (String) params.get("choice");
        String questionId = (String) params.get("questionId");
        QuestionSetEntity entity = new QuestionSetEntity();

        switch (choice) {
            case "A":
                entity.setChoiceaNext(questionId);
                break;
            case "B":
                entity.setChoicebNext(questionId);
                break;
            case "C":
                entity.setChoicecNext(questionId);
                break;
            case "D":
                entity.setChoicedNext(questionId);
                break;
            case "E":
                entity.setChoiceeNext(questionId);
                break;
            case "W":
                entity.setChoiceWrongNext(questionId);
                break;
            case "R":
                entity.setChoiceRightNext(questionId);
                break;
        }

        entity.setId(id);
        entity.setUpdateTime(DateUtils.getTimestamp());
        return answerManager.updateSetLink(entity);
    }

    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:post/answer/saveOrUpdate')")
    @PostMapping("/saveSet")
    public Result saveSet(@RequestBody Map<String, Object> params) {
        String title = (String) params.get("title");
        String startVideoId = (String) params.get("startVideoId");
        String endVideoId = (String) params.get("endVideoId");
        List<String> idsArr = (ArrayList) params.get("ids");
        //
        List<QuestionSetEntity> list = new ArrayList<>();
        QuestionSetEntity entity = null;
        //生成唯一标识
        String uuid = generateShortUuid();
        Integer orgId = SysUserUtil.getLoginOrgId();
        for (int i = 0; i < idsArr.size(); i++) {
            entity = new QuestionSetEntity();
            entity.setQuestionId(Integer.parseInt(idsArr.get(i)));
            entity.setQuestionLibId(uuid);
            entity.setQuestionLibName(title);
            entity.setQuestionLevel(i);
            //默认下一个
            if (i < idsArr.size() - 1) {
                entity.setDefaultNext(idsArr.get(i + 1));
            } else {
                entity.setDefaultNext(null);
            }
            entity.setCreateTime(DateUtils.getTimestamp());
            entity.setUpdateTime(DateUtils.getTimestamp());
            entity.setOrgId(orgId);
            list.add(entity);
        }
        // 如果都不为空 则插表
        if (StringUtils.isNotEmpty(endVideoId) || StringUtils.isNotEmpty(startVideoId)) {
            QuestionsVideoRelationEntity questionsVideoRelationEntity = new QuestionsVideoRelationEntity();
            questionsVideoRelationEntity.setOrgId(SysUserUtil.getLoginOrgId());
            questionsVideoRelationEntity.setQuestionLibId(uuid);
            questionsVideoRelationEntity.setStartVideoSerial(startVideoId);
            questionsVideoRelationEntity.setEndVideoSerial(endVideoId);
            questionsVideoRelationMapper.insert(questionsVideoRelationEntity);
        }
        return answerManager.saveSet(list);
    }


    /**
     * 修改用户状态
     *
     * @param params
     * @return
     * @author gitgeek
     */
    @ApiOperation(value = "修改题库状态")
    @GetMapping("/answer/updateEnabled")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionLibId", value = "题库id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "enabled", value = "是否启用", required = true, dataType = "Boolean")
    })
    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('answer:post/answer/saveOrUpdate')")
    public Result updateEnabled(@RequestParam Map<String, Object> params) {
        return answerService.updateEnabled(params);
    }

    /**
     * 查询统计信息
     *
     * @return
     * @author gitgeek
     */
    @ApiOperation(value = "查询单题选项统计信息")
    @GetMapping("/answer/queryStatic")
    @PreAuthorize("hasAuthority('answer:query')")
    public Result queryStatic(@RequestParam Map<String, Object> params) {
        String questionTitle = (String) params.get("questionTitle");
        String setId = (String) params.get("setId");
        if (StringUtils.isEmpty(questionTitle)) {
            return Result.failed("系统错误");
        }
        return answerService.queryStatic(questionTitle, setId);
    }

    /**
     * 查询统计信息
     *
     * @return
     * @author gitgeek
     */
    @ApiOperation(value = "查询题目被抽到的统计信息")
    @GetMapping("/answer/queryQuestionStatic")
    @PreAuthorize("hasAuthority('answer:query')")
    public Result queryQuestionStatic(@RequestParam Map<String, Object> params) {
        String questionLibName = (String) params.get("questionLibName");
        return answerService.queryQuestionStatic(questionLibName);
    }

    /**
     * 查询套题列表
     *
     * @return
     * @author gitgeek
     */
    @ApiOperation(value = "查询题目被抽到的统计信息")
    @GetMapping("/answer/querySetList")
    @PreAuthorize("hasAuthority('answer:query')")
    public Result querySetList() {
        List<QuestionSetEntity> list = questionSetMapper.findList(new HashMap<>());
        return Result.succeed(JSON.toJSONString(list), "操作成功");
    }


    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }
}
