package com.zay.crowd.api;

import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.po.MemberPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="database-provider")
public interface DataBaseOperationRemoteService {

    /**
     * 查询数据库中是否有对应名字的数据，并烦恼会这条数据对象
     * @param loginAcct
     * @return
     */
    @RequestMapping("/retrieve/member/by/login/acct")
    ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct);

    /**
     * 查询数据库中有没有这个登录名的数据，并返回条数
     * @param loginAcct
     * @return
     */
    @RequestMapping("/retrieve/loign/acct/count")
    ResultEntity<Integer> retrieveLoignAcctCount(@RequestParam("loginAcct") String loginAcct);

    /**
     * 保存用户信息到数据库
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO);
}
