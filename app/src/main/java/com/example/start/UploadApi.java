package com.example.start;

import com.android.volley.Request;

import java.util.List;

public class UploadApi {

    /**
     * 上传图片接口
     * @param listener 请求回调
     */
    public static void uploadImg(List<FormImg> imageList, ResponseListener listener){
        Request request = new PostUploadRequest(Constant.UploadHost,imageList,listener) ;
        VolleyUtil.getRequestQueue().add(request) ;
    }
}