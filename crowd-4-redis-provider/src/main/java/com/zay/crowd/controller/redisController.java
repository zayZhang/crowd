package com.zay.crowd.controller;

import com.zay.crowd.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class redisController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 通过可以查询项目的信息，返回的是json
     * @param normalKey
     * @return
     */
    @RequestMapping("/retrieve/string/value/by/string/key")
    ResultEntity<String> retrieveStringValueByStringKey(@RequestParam("normalKey") String normalKey){
        String jsonString=null;
        try{
            jsonString=redisTemplate.opsForValue().get(normalKey);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return ResultEntity.successWithData(jsonString);
    }

    /**
     *  封装通用的保存String类型键值对的Redis操作方法
     * @param normalKey
     * @param normalValue
     * @return
     */
    @RequestMapping("/save/normal/string/key/value")
    ResultEntity<String> saveNormalStringKeyValue(@RequestParam("normalKey") String normalKey,
                                                  @RequestParam("normalValue") String normalValue){

        try {
            redisTemplate.opsForValue().set(normalKey, normalValue);
        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.faild(e.getMessage());
        }

        return ResultEntity.successNoData();
    }

    @RequestMapping("/retrieve/token/of/signed/member/remote")
    ResultEntity<String> retrieveTokenOfSignedMemberRemote(@RequestParam("memberSignToken") String memberSignToken){
        String memberId=null;
        try {
            memberId=redisTemplate.opsForValue().get(memberSignToken);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }

        return ResultEntity.successWithData(memberId);
    }

    @RequestMapping("/save/token/of/signed/member/remote")
    ResultEntity<String> saveTokenOfSignedMemberRemote(@RequestParam("token") String tokenAsKey,
                                                       @RequestParam("memberId") Integer memberIdAsValue){
        try{
            //将token令牌和用户登录的id存储菁redis，存活时间是一个小时
            redisTemplate.opsForValue().set(tokenAsKey,memberIdAsValue+"",60, TimeUnit.MINUTES);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return ResultEntity.successNoData();
    }

    @RequestMapping("/remove/random/code/remote")
    ResultEntity<String> removeRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey){
        try{
          redisTemplate.delete(randomCodeKey);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return ResultEntity.successNoData();
    }

    @RequestMapping("/retrieve/random/code/remote")
    ResultEntity<String> retrieveRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey){
        String randomCodeValue;
        try {
            randomCodeValue = redisTemplate.opsForValue().get(randomCodeKey);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return ResultEntity.successWithData(randomCodeValue);
    }

    /**
     * 发送短信，并将短信验证码保存进redis
     * @param randomCodeKey
     * @param randomCodeValue
     * @return
     */
    @RequestMapping("/save/random/code/remote")
    ResultEntity<String> saveRandomCodeRemote(
            @RequestParam("randomCodeKey") String randomCodeKey,
            @RequestParam("randomCodeValue") String randomCodeValue){
        try{
            redisTemplate.opsForValue().set(randomCodeKey,randomCodeValue);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return ResultEntity.successNoData();
    }

}
