package com.wanou.system.tools;

import com.wanou.system.util.ConstantUtils;
import org.slf4j.Marker;
import java.util.HashMap;
import java.util.List;

/**
 * ResponseBody构造器。一般用于ajax、rest等类型的Web服务
 */
@SuppressWarnings("serial")
public class RestResponse extends HashMap<String, Object> {


    public static RestResponse success(boolean status,String message){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(status);
        restResponse.setMsg(message);
        return restResponse;
    }

    public static RestResponse failure(boolean status,String message){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(status);
        restResponse.setMsg(message);
        return restResponse;
    }

    public static RestResponse msgData (List<?> data){
        RestResponse restResponse = new RestResponse();
        restResponse.setData(data);
        return restResponse;
    }

    public static RestResponse msgReturn (Long count,List<?> data){
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(0);
        restResponse.setMsg("");
        restResponse.setCount(count);
        restResponse.setData(data);
        return restResponse;
    }


    public RestResponse setSuccess(Boolean success) {
        if (success != null) put("success", success);
        return this;
    }

    public RestResponse setMsg(String msg) {
        if (msg != null) put("msg", msg);
        return this;
    }

    public RestResponse setData(Object data) {
        if (data != null) put("data", data);
        return this;
    }

    public RestResponse setCount(Long count) {
        if (count != null) put("count", count);
        return this;
    }

    public RestResponse setCode(Integer code) {
        if (code != null) put("code", code);
        return this;
    }
}
