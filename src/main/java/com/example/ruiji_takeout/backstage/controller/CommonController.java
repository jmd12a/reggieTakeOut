package com.example.ruiji_takeout.backstage.controller;

import com.example.ruiji_takeout.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Jmd
 * @create 2023-03-2023/3/7-9:45
 * @Description：
 */

@RequestMapping("/common")
@Controller
public class CommonController {

    @Value("${reggie.path}") // 从springboot的配置文件中取值
    private String basePath;

    @PostMapping("/upload")
    public @ResponseBody R upload(@RequestParam("file") MultipartFile imageFile) throws IOException {

        String originalFilename = imageFile.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
        String fileName = UUID.randomUUID().toString()+fileType;

        // 判断basePath目录是否存在，path既可以指文件目录，也可以指文件
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs(); // 如果不存在，就创建一个
        }

        String path = basePath +fileName;
        File file = new File(path);
        imageFile.transferTo(file);


        return R.success(fileName);
    }

    @GetMapping("/download")
    public  void download(@RequestParam("name") String fileName, HttpServletResponse response) throws IOException {
        // 设置响应头信息，这种方式的响应头信息和上一种方式是相同的,Content-Disposition是告诉浏览器如何展示响应信息
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);

        // 拼接文件路径
        String filePath = basePath + fileName;

        // 获取文件输入流
        FileInputStream fileInputStream = new FileInputStream(filePath);

        // 获取输出流，通过这个输出流往浏览器写数据
        ServletOutputStream outputStream = response.getOutputStream();

        byte[] buff = new byte[256]; // 一次读整个文件的信息太多，建立一个256大小的byte数组来多次读入

        int len = 0;
        while (true){
            len = fileInputStream.read(buff);
            if (len != -1) {
                outputStream.write(buff,0,len);
                outputStream.flush(); // 刷新输出流，防止没有全部输出
            }else break;
        }

        fileInputStream.close(); // 读取完毕，关闭输入流
        outputStream.close(); // 关闭输出流
    }
}
