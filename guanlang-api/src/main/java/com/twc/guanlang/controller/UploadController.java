package com.twc.guanlang.controller;

import com.twc.guanlang.entity.ChatImage;
import com.twc.guanlang.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**上传文件控制器
 * @author chenqiang
 */
@Controller(value = "/chat")
public class UploadController {


    @Value("${upload.path}")
    private String uploadPath;
    @Value("${file.url}")
    private String fileUrl;

    @Resource
    private UploadService uploadService;

    /**
     * 文件上传--手动运维工单中的图片和视频
     * @param srcFile
     * @param redirectAttributes
     * @return
     */
    @PostMapping("upload")
    @ResponseBody
    public Map fileUpload(@RequestParam("file") MultipartFile srcFile, RedirectAttributes redirectAttributes) {
//前端没有选择文件，srcFile为空

        Map map = new HashMap();
        if (srcFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择一个文件");
            map.put("code", "error");
            map.put("reason", "请选择一个文件");
            return map;
        }
        //选择了文件，开始上传操作
        try {
            //构建上传目标路径，找到了项目的target的classes目录
            File destFile = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!destFile.exists()) {
                destFile = new File("");
            }

            //输出目标文件的绝对路径
            System.out.println("file path:" + destFile.getAbsolutePath());
            //拼接子路径
            SimpleDateFormat sf_ = new SimpleDateFormat("yyyyMMddHHmmss");
            String times = sf_.format(new Date());
            File upload = new File(uploadPath);
            //若目标文件夹不存在，则创建
            if (!upload.exists()) {
                upload.mkdirs();
            }

            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 获得文件原始名称
            String fileName = srcFile.getOriginalFilename();
            // 获得文件后缀名称
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            // 生成最新的uuid文件名称
            String newFileName = uuid + "." + suffixName;
            redirectAttributes.addFlashAttribute("message", "文件上传成功" + newFileName);
            //根据srcFile大小，准备一个字节数组
            byte[] bytes = srcFile.getBytes();
            //拼接上传路径
            //Path path = Paths.get(UPLOAD_FOLDER + srcFile.getOriginalFilename());
            //通过项目路径，拼接上传路径
            Path path = Paths.get(upload.getAbsolutePath() + "/" + newFileName);
            //** 开始将源文件写入目标地址
            Files.write(path, bytes);
            ChatImage chatImage = new ChatImage();
            chatImage.setFromUser("");
            chatImage.setToUser("");
            chatImage.setName(newFileName);
            uploadService.save(chatImage);

            map.put("fileUrl", fileUrl + newFileName);
        } catch (IOException e) {
            map.put("msg", "上传失败");
            map.put("code", "500");
        }
        map.put("code", "success");
        return map;
    }
}