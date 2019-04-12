package com.zay.crowd.service.impl;

import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.entity.po.MemberPOExample;
import com.zay.crowd.mapper.MemberPOMapper;
import com.zay.crowd.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public Integer queryLoginAcctCount(String LoginAcct) {
        MemberPOExample memberPOExample=new MemberPOExample();
        memberPOExample.createCriteria().andLoginacctEqualTo(LoginAcct);
        Integer num=memberPOMapper.countByExample(memberPOExample);
        return num;
    }

    @Override
    public void saveMember(MemberPO memberPo) {
        memberPOMapper.insert(memberPo);
    }

    @Override
    public MemberPO queryMemberVOByLoginacct(String loginAcct) {
        MemberPOExample memberPOExample=new MemberPOExample();
        memberPOExample.createCriteria().andLoginacctEqualTo(loginAcct);
        List<MemberPO> memberPOS = memberPOMapper.selectByExample(memberPOExample);
        if(memberPOS.size()<=0){
            return null;
        }
        System.out.println("              2222222222222222222                  "+memberPOS.get(0).getLoginacct());
        return memberPOS.get(0);
    }
}
