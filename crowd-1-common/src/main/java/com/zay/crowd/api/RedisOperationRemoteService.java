package com.zay.crowd.api;

import com.zay.crowd.entity.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("redis-provider")
public interface RedisOperationRemoteService {

    /**
     * 通过可以查询redis中对应key的值，返回的是json字符串
     * @param normalKey
     * @return
     */
    @RequestMapping("/retrieve/string/value/by/string/key")
    ResultEntity<String> retrieveStringValueByStringKey(@RequestParam("normalKey") String normalKey);

    /**
     * 通用的保存键值对的方法
     * @param normalKey
     * @param normalValue
     * @return
     */
    @RequestMapping("/save/normal/string/key/value")
    ResultEntity<String> saveNormalStringKeyValue(@RequestParam("normalKey") String normalKey,
                                                  @RequestParam("normalValue") String normalValue);

    /**
     * 检查是否登陆
     * @param memberSignToken
     * @return
     */
    @RequestMapping("/retrieve/token/of/signed/member/remote")
    ResultEntity<String> retrieveTokenOfSignedMemberRemote(@RequestParam("memberSignToken") String memberSignToken);

    /**
     * 保存token键值对
     * @param tokenAsKey
     * @param memberIdAsValue
     * @return
     */
    @RequestMapping("/save/token/of/signed/member/remote")
    ResultEntity<String> saveTokenOfSignedMemberRemote(@RequestParam("token") String tokenAsKey,
                                                       @RequestParam("memberId") Integer memberIdAsValue);

    /**
     * 发送验证码短信
     * @param randomCodeKey
     * @param randomCodeValue
     * @return
     */
    @RequestMapping("/save/random/code/remote")
    ResultEntity<String> saveRandomCodeRemote(
            @RequestParam("randomCodeKey") String randomCodeKey,
            @RequestParam("randomCodeValue") String randomCodeValue);

    /**
     * 查询验证码
     */
    @RequestMapping("/retrieve/random/code/remote")
    ResultEntity<String> retrieveRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey);


    /**
     * 删除验证码
     */
    @RequestMapping("/remove/random/code/remote")
    ResultEntity<String> removeRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey);

}
