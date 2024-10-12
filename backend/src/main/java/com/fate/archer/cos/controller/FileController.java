package com.fate.archer.cos.controller;

import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.utils.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    // private final String BASELOCALPATH = "/usr/project/pro_cos/";

    /**
     * 上传图片
     * @param file 上传的文件
     * @return 文件名称
     */
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String upload(@RequestParam("avatar") MultipartFile file) {
        // 1定义要上传文件 的存放路径
        String localPath = BusinessConstant.FILEADDRESS + "db/image";
        // 2获得文件名字
        String fileName = file.getOriginalFilename();
        // 2上传失败提示
        String warning = "";
        String newFileName = FileUtils.upload(file, localPath, fileName);
        if (newFileName != null) {
            //上传成功
            warning = newFileName;

        } else {
            warning = "上传失败";
        }
        System.out.println(warning);
        return warning;
    }

    /**
     * 上传视频
     *
     * @param file 上传的文件
     * @return 文件名称
     */
    @ResponseBody
    @RequestMapping("/fileUploadByVideo")
    public String uploadByVideo(@RequestParam("video") MultipartFile file) {
        // 1定义要上传文件 的存放路径
        String localPath = BusinessConstant.FILEADDRESS + "db/video";
        // 2获得文件名字
        String fileName = file.getOriginalFilename();
        // 2上传失败提示
        String warning = "";
        String newFileName = FileUtils.uploadByVideo(file, localPath, fileName);
        if (newFileName != null) {
            //上传成功
            warning = newFileName;

        } else {
            warning = "上传失败";
        }
        return warning;
    }

    /**
     * 上传项目文件
     *
     * @param file 项目文件
     * @return 压缩包名称
     */
    @ResponseBody
    @RequestMapping("/fileUploadByZip")
    public String uploadByZip(@RequestParam("zip") MultipartFile file) {
        // 1定义要上传文件 的存放路径
        String localPath = BusinessConstant.FILEADDRESS + "file";
        // 2获得文件名字
        String fileName = file.getOriginalFilename();
        // 2上传失败提示
        String warning = "";
        String newFileName = FileUtils.uploadByZip(file, localPath, fileName);
        if (newFileName != null) {
            //上传成功
            warning = newFileName;

        } else {
            warning = "上传失败";
        }
        return warning;
    }

    /**
     * 上传项目附件文件
     *
     * @param file 附件文件
     * @return 附件名称
     */
    @ResponseBody
    @RequestMapping("/fileUploadByAnnex")
    public String uploadByAnnex(@RequestParam("annex") MultipartFile file) {
        // 1定义要上传文件 的存放路径
        String localPath = BusinessConstant.FILEADDRESS + "db/annex";
        // 2获得文件名字
        String fileName = file.getOriginalFilename();
        // 2上传失败提示
        String warning = "";
        String newFileName = FileUtils.uploadByAnnex(file, localPath, fileName);
        if (newFileName != null) {
            //上传成功
            warning = newFileName;

        } else {
            warning = "上传失败";
        }
        return warning;
    }

}
