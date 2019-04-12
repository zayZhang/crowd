package com.zay.crowd.Controller;

import com.zay.crowd.api.DataBaseOperationRemoteService;
import com.zay.crowd.api.RedisOperationRemoteService;
import com.zay.crowd.constants.CrowdConstant;
import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.UUID;

@Controller
public class ProgramController {

    @Autowired
    private RedisOperationRemoteService operationRemoteService;

    @Autowired
    private DataBaseOperationRemoteService dataBaseOperationRemoteService;

    /**
     * 保存详情图片
     * @param memberSignToken
     * @param projectTempToken
     * @param detailPicturePathList
     * @return
     */
    @RequestMapping("/save/detail/picture/path/list")
    public ResultEntity<String> saveDetailPicturePathList(
            @RequestParam("memberSignToken") String memberSignToken,
            @RequestParam("projectTempToken") String projectTempToken,
            @RequestParam("detailPicturePathList") List<String> detailPicturePathList) {
        // 检查登陆状态
        ResultEntity<String> stringResultEntity = operationRemoteService.retrieveTokenOfSignedMemberRemote(memberSignToken);
        if(ResultEntity.FAILD.equals(stringResultEntity.getResult())){
            return ResultEntity.faild(stringResultEntity.getMessage());
        }

        if(stringResultEntity.getData()==null){
            return ResultEntity.faild(stringResultEntity.getMessage());
        }
        ResultEntity<String> jsonString=operationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if (jsonString.getData()==null||ResultEntity.FAILD.equals(jsonString.getResult())){
            return ResultEntity.faild(CrowdConstant.MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE);
        }
        ProjectVO project=JSON.parseObject(jsonString.getData(),ProjectVO.class);

        project.setDetailPicturePathList(detailPicturePathList);

        String json=JSON.toJSONString(project);

        return operationRemoteService.saveNormalStringKeyValue(projectTempToken,json);
    }

    /**
     * 保存头图
     * @param memberSignToken
     * @param projectTempToken
     * @param headerPicturePath
     * @return
     */
    @RequestMapping("/save/header/picture/path")
    public ResultEntity<String> saveHeaderPicturePath(
            @RequestParam("memberSignToken") String memberSignToken,
            @RequestParam("projectTempToken") String projectTempToken,
            @RequestParam("headerPicturePath") String headerPicturePath) {
        ResultEntity<String> stringResultEntity = operationRemoteService.retrieveTokenOfSignedMemberRemote(memberSignToken);
        if(ResultEntity.FAILD.equals(stringResultEntity.getResult())){
            return ResultEntity.faild(stringResultEntity.getMessage());
        }

        if(stringResultEntity.getData()==null){
            return ResultEntity.faild(stringResultEntity.getMessage());
        }

        ResultEntity<String> result=operationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if(ResultEntity.FAILD.equals(result.getResult())){
            return ResultEntity.faild(CrowdConstant.MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE);
        }

        String jsonString=result.getData();
        ProjectVO project=JSON.parseObject(jsonString,ProjectVO.class);
        project.setHeaderPicturePath(headerPicturePath);
        String json=JSON.toJSONString(project);
        return operationRemoteService.saveNormalStringKeyValue(projectTempToken,json);
    }


    /**
     * 初始化项目信息，保存了一个项目的id号码，将用户登录时存的redis中的key作为项目id和对应用户关联
     * @param memberSignToken
     * @return
     */
    @RequestMapping("/init/project/create")
    public ResultEntity<String> initProjectCreate(@RequestParam("memberSignToken") String memberSignToken) {
        //检查是否登录(通过查询redis中是否存有对应的token)
        ResultEntity<String> stringResultEntity = operationRemoteService.retrieveTokenOfSignedMemberRemote(memberSignToken);
        if(ResultEntity.FAILD.equals(stringResultEntity.getResult())){
            return ResultEntity.faild(stringResultEntity.getMessage());
        }
        if(stringResultEntity.getData()==null){
            return ResultEntity.faild(stringResultEntity.getMessage());
        }
        ProjectVO projectVO=new ProjectVO();
        projectVO.setMemberSignToken(memberSignToken);

        String key= CrowdConstant.REDIS_PROJECT_TEMP_TOKEN_PREFIX + UUID.randomUUID().toString().replaceAll("-", "");

        // ※REDIS_PROJECT_TEMP_TOKEN_PREFIX也需要设置到ProjectVO对象中
        //这里是因为我们存进redis是村的hash类型，hash中不可能配套list的，因为一个值对应一个键，
        //所以就把整个projectVO作为整个值传进去就可以啦
        projectVO.setProjectTempToken(key);

        String value=JSON.toJSONString(projectVO);

        return  operationRemoteService.saveNormalStringKeyValue(key,value);
    }
}
