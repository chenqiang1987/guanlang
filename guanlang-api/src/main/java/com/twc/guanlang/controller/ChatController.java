package com.twc.guanlang.controller;

import com.twc.guanlang.entity.CallLine;
import com.twc.guanlang.entity.Caller;
import com.twc.guanlang.entity.ChatImage;
import com.twc.guanlang.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chenqiang
 * 上传文件控制器
 * 直接上传到服务器
 */

@Controller
public class ChatController {
    /**
     * 指定一个临时路径作为上传目录
     * private static String UPLOAD_FOLDER = "C:\Users\Liuyu\Desktop\UPLOAD";
     * 遇到http://localhost:8080，则跳转至upload.html页面
     **/
//    @GetMapping("/")
//    public String index() {
//        return "upload";
//    }


    //本地测试暂map，线上换成redis,用户下线需要删除，并且通知对方

    /**
     * callCenter
     */
    public static List<CallLine> lines = new ArrayList<>();


    /**
     * 通讯录
     */
    public static List<Caller> photos = new ArrayList<>();

    /**
     * 连接是否关闭
     *
     * @param fromUser
     * @param toUser
     * @return
     */
    @GetMapping("connectionOpen2Close")
    @ResponseBody
    public Map connectionOpen2Close(@RequestParam("toUser") String toUser, @RequestParam("fromUser") String fromUser, @RequestParam("mySdp") String mySdp) {


        return null;

    }


    /**
     * 注册sdp
     *
     * @param userName
     * @param mySdp
     * @return
     */
    @GetMapping("registerMySdp")
    @ResponseBody
    public Map registerMySdp(@RequestBody Caller caller) {

        /*
            当前用户已经注册sdp,说明获取了新的sdp
            根据sdp的不能复用的特性，需要删除之前建立的关系
         */
        if (!photos.contains(caller)) {

            photos.add(caller);

        }
        Map map = new HashMap();
        map.put("code", 200);
        map.put("msg", "success");
        return map;

    }


    /**
     * @return
     */
    @GetMapping("call")
    @ResponseBody
    public Map call(@RequestBody Caller fromUser, @RequestBody Caller toUser) {


        Map map = new HashMap();
        map.put("code", 200);
        map.put("msg", "success");
        return map;

    }


    @GetMapping("refreshLine")
    @ResponseBody
    public Map refreshLine(@RequestBody CallLine callLine) {
        if (!lines.contains(callLine)) {
            lines.add(callLine);
        }
        Map map = new HashMap();
        map.put("code", 200);
        map.put("msg", "success");
        return map;

    }


    /**
     * 查询呼叫中心
     *
     * @param userName
     * @return
     */
    @GetMapping("looUpCallCenter")
    @ResponseBody
    public Map looUpCallCenter(@RequestBody Caller caller) {

        Map map = new HashMap();

        if (lines.size() == 0) return null;

        for (CallLine callLine : lines) {

            //被呼叫
            if (callLine.getTo().getUserName().equals(caller.getUserName()) && callLine.getConnectStatus() == CallLine.STATUS.WAIT_TO) {
                Caller toCaller = callLine.getTo();

                //设置被叫sdp
                toCaller.setUserSdp(caller.getUserSdp());
                //设置等待被叫确认
                callLine.setConnectStatus(CallLine.STATUS.WAIT_FROM);

                //更新line信息
                map.put("line", callLine);
                map.put("code", 200);
                map.put("msg", "success");
                return map;
            }
        }

        return map;

    }


    //匹配upload_status页面
    @GetMapping("upload_status")
    public String uploadStatusPage() {
        return "upload_status";
    }
}

