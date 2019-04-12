package com.zay.crowd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity <T> {

    private String result;
    private String message;
    private T data;

    public final static String SUCCESS="SUCCESS";
    public final static String FAILD="FAILD";
    public final static String NO_MSG="MO_MESSAGE";
    public final static String NO_DATA="NO_DATA";

    public static ResultEntity<String> successNoData(){
        return new ResultEntity(SUCCESS,NO_MSG,NO_DATA);
    }

    public static <T> ResultEntity<T> successWithData(T t){
        return new ResultEntity(SUCCESS,NO_MSG,t);
    }

    public static <T> ResultEntity<T> faild(String msg){
        return new ResultEntity(FAILD,msg,NO_DATA);
    }

}
