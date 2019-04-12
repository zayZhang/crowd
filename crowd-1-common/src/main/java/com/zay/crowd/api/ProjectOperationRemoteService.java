package com.zay.crowd.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value="project-manager")
public interface ProjectOperationRemoteService {


}


