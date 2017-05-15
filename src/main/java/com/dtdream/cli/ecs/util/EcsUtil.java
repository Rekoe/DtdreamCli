package com.dtdream.cli.ecs.util;

import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by thomugo on 2016/10/18.
 */
public class EcsUtil {
    public EcsUtil(){}
    public static List<DescribeInstancesResponse.Instance> getInstance(String instanceId, String instanceName){
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        if(instanceId != null){
            request.setInstanceIds(instanceId);
        }
        if(instanceName != null){
            request.setInstanceName(instanceName);
        }
        List<DescribeInstancesResponse.Instance> list = null;
        try{
            DescribeInstancesResponse response = EcsClient.getInstance().getAcsResponse(request);
            list = response.getInstances();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static DescribeInstanceStatusResponse.InstanceStatus getInstanceStatus(String instanceId){
        DescribeInstanceStatusResponse.InstanceStatus retStatus = null;
        DescribeInstanceStatusRequest request = new DescribeInstanceStatusRequest();
        try{
            DescribeInstanceStatusResponse response = EcsClient.getInstance().getAcsResponse(request);
            List<DescribeInstanceStatusResponse.InstanceStatus> list = response.getInstanceStatuses();
            for (DescribeInstanceStatusResponse.InstanceStatus instanceStatus : list){
                if(instanceStatus.getInstanceId().equals(instanceId)){
                    retStatus = instanceStatus;
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return retStatus;
    }

    public static List<String> getSupportDisks(String regionId, String zoneId){
        List<String> disks = null;
        DescribeZonesRequest request = new DescribeZonesRequest();
        request.setRegionId(regionId);
        try{
            DescribeZonesResponse response = EcsClient.getInstance().getAcsResponse(request);
            List<DescribeZonesResponse.Zone> zones = response.getZones();
            for(DescribeZonesResponse.Zone zone : zones){
                if(StringUtils.equals(zone.getZoneId(), zoneId)){
                    disks = zone.getAvailableDiskCategories();
                }
            }
            if(disks == null){
                System.out.println("ZoneID 有错，不存在此可用区！！！");
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return disks;
        }
    }

    public static DescribeDisksResponse.Disk getDisk(String diskId){
        DescribeDisksRequest request = new DescribeDisksRequest();
        request.setDiskIds(toJsonStr(diskId));
        DescribeDisksResponse.Disk disk = null;
        try{
            DescribeDisksResponse response = EcsClient.getInstance().getAcsResponse(request);
            disk = response.getDisks().get(0);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return disk;
        }
    }
    public static boolean getDiskPortable(String diskId){
        boolean ret = false;
        DescribeDisksRequest request = new DescribeDisksRequest();
        request.setDiskIds(toJsonStr(diskId));
        try{
            DescribeDisksResponse response = EcsClient.getInstance().getAcsResponse(request);
            DescribeDisksResponse.Disk disk = response.getDisks().get(0);
            if(disk != null){
                ret = disk.getPortable();
            }else{
                System.out.printf("不存在ID为: %s 的磁盘", diskId);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }

    public static String getDiskStatus(String diskId){
        String ret = null;
        DescribeDisksRequest request = new DescribeDisksRequest();
        request.setDiskIds(toJsonStr(diskId));
        try{
            DescribeDisksResponse response = EcsClient.getInstance().getAcsResponse(request);
            DescribeDisksResponse.Disk disk = response.getDisks().get(0);
            if(disk != null){
                ret = disk.getStatus();
            }else{
                System.out.printf("不存在ID为: %s 的磁盘", diskId);

            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }
    
    public static DescribeSnapshotsResponse.Snapshot getSnapshot(String snapshotId){
        DescribeSnapshotsResponse.Snapshot ret = null;
        DescribeSnapshotsRequest request = new DescribeSnapshotsRequest();
        request.setSnapshotIds(toJsonStr(snapshotId));
        try{
            DescribeSnapshotsResponse response = EcsClient.getInstance().getAcsResponse(request);
            ret = response.getSnapshots().get(0);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }
    
    public static DescribeSecurityGroupsResponse.SecurityGroup getSecurityGroup(String securityGroupId){
        DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
        DescribeSecurityGroupsResponse.SecurityGroup ret = null;
        try{
            DescribeSecurityGroupsResponse response = EcsClient.getInstance().getAcsResponse(request);
            List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroups = response.getSecurityGroups();
            for(DescribeSecurityGroupsResponse.SecurityGroup securityGroup : securityGroups){
                if(StringUtils.equals(securityGroup.getSecurityGroupId(),securityGroupId)){
                    ret = securityGroup;
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }

    public static List<DescribeVSwitchesResponse.VSwitch> getVSwitchs(String vpcId){
        List<DescribeVSwitchesResponse.VSwitch> vSwitches = null;
        DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
        request.setVpcId(vpcId);
        try{
            DescribeVSwitchesResponse response = EcsClient.getInstance().getAcsResponse(request);
            vSwitches = response.getVSwitches();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return vSwitches;
        }
    }

    public static String toJsonStr(String diskIds){
        String [] disks = diskIds.split(",");
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (String str : disks){
            builder.append('"');
            builder.append(str);
            builder.append('"');
            builder.append(',');
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(']');
        String jsonStr = builder.toString();
        return jsonStr;
    }

}
