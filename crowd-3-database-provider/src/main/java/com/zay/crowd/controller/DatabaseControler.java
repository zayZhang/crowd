package com.zay.crowd.controller;

import com.zay.crowd.constants.CrowdConstant;
import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseControler {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/retrieve/member/by/login/acct")
    ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct){
        MemberPO memberPO=memberService.queryMemberVOByLoginacct(loginAcct);
        if (memberPO==null){
            return ResultEntity.faild(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        return ResultEntity.successWithData(memberPO);
    }

    @RequestMapping("/retrieve/loign/acct/count")
    public ResultEntity<Integer> retrieveLoignAcctCount(@RequestParam("loginAcct") String loginAcct){
        Integer count=null;
        try{
            count=memberService.queryLoginAcctCount(loginAcct);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return ResultEntity.successWithData(count);
    }

    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO){
        try {
            memberService.saveMember(memberPO);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return ResultEntity.successNoData();
    }
}
