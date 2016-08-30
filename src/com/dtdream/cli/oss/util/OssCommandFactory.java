package com.dtdream.cli.oss.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.*;

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
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' oss -help '查找帮助");
                break;
        }
        return null;
    }

}
