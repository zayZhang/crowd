package com.zay.crowd.utils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;

public class UploadUtils {
    /**
     * 生成文件名
     *通过UUID随机生成一个数，然后将图片文件名去除，留下后缀名做拼接
     * @param originalFilename
     * @return
     */
    public static String generateFileName(String originalFilename) {
        String fileNames=UUID.randomUUID().toString().replaceAll("-","");
        String Name=originalFilename.substring(originalFilename.lastIndexOf("."));
        return fileNames+Name;
    }

    /**
     * 根据当前天日期生成目录名
     *
     * @return
     */
    public static String generateDayFolderName() {

        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * 上传单个文件到OSS
     *
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @param fileName
     * @param folderName
     * @param bucketName
     * @param inputStream
     */
    public static void uploadSingleFile(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            String fileName,
            String folderName,
            String bucketName,
            InputStream inputStream) {
        try {
            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 存入对象的名称=目录名称+"/"+文件名
            String objectName = folderName + "/" + fileName;
            ossClient.putObject(bucketName, objectName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (OSSException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
