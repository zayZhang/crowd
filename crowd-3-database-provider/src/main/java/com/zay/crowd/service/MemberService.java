package com.zay.crowd.service;

import com.zay.crowd.entity.po.MemberPO;

public interface MemberService {

    public Integer queryLoginAcctCount(String LoginAcct);

    public void saveMember(MemberPO memberPo);

    MemberPO queryMemberVOByLoginacct(String loginAcct);
}
