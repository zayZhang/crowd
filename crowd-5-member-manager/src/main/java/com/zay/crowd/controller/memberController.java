package com.zay.crowd.controller;

import com.zay.crowd.api.DataBaseOperationRemoteService;
import com.zay.crowd.api.RedisOperationRemoteService;
import com.zay.crowd.constants.CrowdConstant;
import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.entity.vo.MemberSignSuccessVO;
import com.zay.crowd.entity.vo.MemberVO;
import com.zay.crowd.utils.CrowdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class memberController {

    @Autowired
    private RedisOperationRemoteService redisOperationRemoteService;

    @Autowired
    private DataBaseOperationRemoteService dataBaseOperationRemoteService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${crowd.short.message.appCode}")
    private String messageString;



    /**
     * 退出登录
     * @param tokenAsKey
     * @return
     */
    @RequestMapping("/remove/token/of/signed/member/remote")
    public ResultEntity<String> removeTokenOfSignedMemberRemote(@RequestParam("token") String tokenAsKey) {
        ResultEntity<String> stringResultEntity=null;
        try{
            stringResultEntity = redisOperationRemoteService.removeRandomCodeRemote(tokenAsKey);
        }catch(Exception e){
            e.printStackTrace();
            return ResultEntity.faild(e.getMessage());
        }
        return stringResultEntity;
    }

    /**
     * 登录操作
     * @param loginacct
     * @param userpswd
     * @return
     */
    @RequestMapping("/member/login")
    public ResultEntity<MemberSignSuccessVO> login(@RequestParam("loginacct") String loginacct,
                                                   @RequestParam("userpswd") String userpswd) {
        ResultEntity<MemberPO> memberPOResultEntity = dataBaseOperationRemoteService.retrieveMemberByLoginAcct(loginacct);
        if(ResultEntity.FAILD.equals(memberPOResultEntity.getResult())){
            return ResultEntity.faild(memberPOResultEntity.getMessage());
        }
        if(memberPOResultEntity.getData()==null){
            return ResultEntity.faild(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        MemberPO memberPO=memberPOResultEntity.getData();
        String password=memberPO.getUserpswd();
        if(!(bCryptPasswordEncoder.matches(userpswd,password))){
            return ResultEntity.faild(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        String tokenAsKey=CrowdUtils.generateToken();
        Integer tokenAsValue=memberPO.getId();
        ResultEntity<String> redisSaveToken=null;
        try{
            redisSaveToken= redisOperationRemoteService.saveTokenOfSignedMemberRemote(tokenAsKey, tokenAsValue);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(ResultEntity.FAILD.equals(redisSaveToken.getResult())){
            return ResultEntity.faild(redisSaveToken.getResult());
        }
        MemberSignSuccessVO memberSignSuccessVO = new MemberSignSuccessVO();
        BeanUtils.copyProperties(memberPO,memberSignSuccessVO);
        memberSignSuccessVO.setToken(tokenAsKey);

        return ResultEntity.successWithData(memberSignSuccessVO);
    }

    /**
     * 保存用户的注册信息
     */
    @RequestMapping("/member/register")
    public ResultEntity<String> register(@RequestBody MemberVO memberVO) {
        //检查验证码合法性
        String randomCode= memberVO.getRandomCode();
        if(!CrowdUtils.strEffectiveCheck(randomCode)){
            return ResultEntity.faild(CrowdConstant.MESSAGE_RANDOM_CODE_INVALID);
        }
        // 2.获取手机号数据并进行有效性检测
        String phone=memberVO.getPhoneNum();
        if(!CrowdUtils.strEffectiveCheck(phone)){
            return ResultEntity.faild(CrowdConstant.MESSAGE_PHONENUM_INVALID);
        }
        // 3.拼接Redis存储验证码的KEY
        String redisRandomKey=CrowdConstant.REDIS_RANDOM_CODE_PREFIX+phone;

        ResultEntity<String> resultEntityRedis=redisOperationRemoteService.retrieveRandomCodeRemote(redisRandomKey);

        if(resultEntityRedis==null){
            return ResultEntity.faild(CrowdConstant.MESSAGE_RANDOM_CODE_OUT_OF_DATE);
        }

        System.out.println(resultEntityRedis.getData());
        // 4.验证验证码
        if(!resultEntityRedis.getData().equals(memberVO.getRandomCode())){
            return ResultEntity.faild(CrowdConstant.MESSAGE_RANDOM_CODE_NOT_MATCH);
        }

        // 5.查询是否有相同用户名的数据
        ResultEntity<Integer> count=dataBaseOperationRemoteService.retrieveLoignAcctCount(memberVO.getLoginacct());
        if(ResultEntity.FAILD.equals(count.getResult())){
            return resultEntityRedis;
        }
        if(count.getData()>0){
            return ResultEntity.faild(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
        }

        String userpswd = memberVO.getUserpswd();

        if(!CrowdUtils.strEffectiveCheck(userpswd)) {
            return ResultEntity.faild(CrowdConstant.MESSAGE_PASSWORD_INVALID);
        }

        userpswd = bCryptPasswordEncoder.encode(userpswd);
        memberVO.setUserpswd(userpswd);
        MemberPO memberPO=new MemberPO();
        BeanUtils.copyProperties(memberVO,memberPO);

        return dataBaseOperationRemoteService.saveMemberRemote(memberPO);
    }

    /**
     * 发送验证码短信
     * @param phoneNum
     * @return
     */
    @RequestMapping("/member/send/code")
    public ResultEntity<String> sendCode(String phoneNum) {
        if(!CrowdUtils.strEffectiveCheck(phoneNum)){
            return ResultEntity.faild(CrowdConstant.MESSAGE_PHONENUM_INVALID);
        }
        String message=CrowdUtils.randomCode(6);
        CrowdUtils.sendShortMessage(messageString,message,phoneNum);
        //验证码所对应的键
        String token=CrowdConstant.REDIS_RANDOM_CODE_PREFIX + phoneNum;

        return redisOperationRemoteService.saveRandomCodeRemote(token,message);
    }

}
