package com.dtdream.cli.oss.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.dtdream.cli.util.OssClient;

import java.util.List;

/**
 * Created by thomugo on 2016/8/29.
 */
public class OssUtil {
    public OssUtil() {
    }

    public static enum StepFlags {
        createBucket,;
        private StepFlags() {
        }
    }

    public static boolean doesBucketExist(String bucketName){
        boolean exists = false;
        try {
            exists = OssClient.getInstance().doesBucketExist(bucketName);
        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        }
        return exists;
    }

    public static boolean doesDirectoryExist(String bucketName, String dirName){
        String name;
        String prefix;
        if(dirName.endsWith("/")){
            dirName = dirName.substring(0,dirName.length()-1);
        }
        if(dirName.contains("/")){
            name = dirName + "/";
            prefix = dirName.substring(0,dirName.lastIndexOf("/") + 1);
        }else{
            prefix = "";
            name = dirName + "/";
        }
        int maxKeys = 100;
        String nextMarker = null;
        ObjectListing objectListing = null;
        do{
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withMarker(nextMarker).withMaxKeys(maxKeys);
            listObjectsRequest.setPrefix(prefix);
            listObjectsRequest.withDelimiter("/");
            objectListing = OssClient.getInstance().listObjects(listObjectsRequest);
            List<String> sums = objectListing.getCommonPrefixes();
            if(sums.contains(name)){
                return true;
            }
            nextMarker = objectListing.getNextMarker();
        }while (objectListing.isTruncated());

        return false;
    }

    public static boolean doesDirectoryEmpty(String bucketName, String dirName){
        String prefix = "";
        if(dirName.equals("")){
            prefix = "";
        }else if(!dirName.endsWith("/")){
            prefix = dirName + "/";
        }
        int maxKeys = 100;
        String nextMarker = null;
        ObjectListing objectListing = null;
        do{
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withMarker(nextMarker).withMaxKeys(maxKeys);
            listObjectsRequest.setPrefix(prefix);
            objectListing = OssClient.getInstance().listObjects(listObjectsRequest);
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            if(sums.size() == 1){
                if(sums.get(0).getKey().equals(prefix)){
                    return true;
                }
            }
            nextMarker = objectListing.getNextMarker();
        }while (objectListing.isTruncated());

        return false;
    }
}
