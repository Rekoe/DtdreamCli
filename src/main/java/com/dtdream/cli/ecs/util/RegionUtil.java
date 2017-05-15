package com.dtdream.cli.ecs.util;

import com.aliyuncs.ecs.model.v20140526.DescribeZonesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by thomugo on 2016/11/3.
 */
public class RegionUtil {
    public RegionUtil(){}
    public static List<DescribeZonesResponse.Zone> getZones(String regionId){
        List<DescribeZonesResponse.Zone> ret = null;
        DescribeZonesRequest request = new DescribeZonesRequest();
        request.setRegionId(regionId);
        try{
            DescribeZonesResponse response = EcsClient.getInstance().getAcsResponse(request);
            ret = response.getZones();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }
    public static List<String> getDiskCategories(String regionId, String zoneId){
        List<String> ret = null;
        DescribeZonesRequest request = new DescribeZonesRequest();
        request.setRegionId(regionId);
        try{
            DescribeZonesResponse response = EcsClient.getInstance().getAcsResponse(request);
            List<DescribeZonesResponse.Zone> zones = response.getZones();
            for (DescribeZonesResponse.Zone zone : zones){
                if(zone.getZoneId().equals(zoneId)){
                    ret = zone.getAvailableDiskCategories();
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
}
