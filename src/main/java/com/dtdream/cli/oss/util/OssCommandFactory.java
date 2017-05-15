package com.dtdream.cli.oss.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.*;
import com.dtdream.cli.oss.bucket.*;
import com.dtdream.cli.oss.download.Download;
import com.dtdream.cli.oss.download.Get;
import com.dtdream.cli.oss.object.*;
import com.dtdream.cli.oss.upload.*;

/**
 * Created by thomugo on 2016/8/29.
 */
public class OssCommandFactory implements ICommandFactory {
    @Override
    public Command getCommand(String [] var) {
        switch (var[0]){
            case "-help" :
            case "--help" : return new Help(this);
            case "createBucket" : return new CreateBucket(this, var);
            case "listBuckets" : return new ListBuckets(this, var);
            case "deleteBucket" : return new DeleteBucket(this, var);
            case "getBucketAcl" : return new GetBucketAcl(this, var);
            case "setBucketAcl" : return new SetBucketAcl(this, var);
            case "getBucketLocation" : return new GetBucketLocation(this, var);
            case "getBucketInfo" : return new GetBucketInfo(this, var);
            case "put" : return new Put(this, var);
            case "append" : return new Append(this, var);
            case "uploadFile" : return new UploadFile(this, var);
            case "read" : return new Read(this, var);
            case "get" : return new Get(this, var);
            case "download" : return new Download(this, var);
            case "setObjectAcl" : return new SetObjectAcl(this, var);
            case "getObjectAcl" : return new GetObjectAcl(this, var);
            case "getObjectMeta" : return new GetObjectMeta(this, var);
            case "listObjects" : return new ListObjects(this, var);
            case "mkdir" : return new Mkdir(this, var);
            case "rm" : return new DeleteObject(this, var);
            case "cp" : return new Copy(this, var);
            case "listUpload" : return new ListMultiUpload(this, var);
            case "abort" : return new AbortUpload(this, var);
            case "genUrl" : return new GeneratePresignedUrl(this, var);
            case "log" : return new Log(this, var);
            case "life" : return new LifeCycle(this, var);
            case "cors" : return new Cors(this, var);
            case "referer" : return new Referer(this, var);
            case "listMultiUpload" : return new ListMultiUpload(this, var);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' oss -help '查找帮助");
                break;
        }
        return null;
    }

}
