package com.zay.crowd.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value="member-manager")
public interface MemberOperationRemoteService {

}
