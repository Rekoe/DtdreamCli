package com.dtdream.cli.logo;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.util.Config;
import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static com.dtdream.cli.util.Config.system;

/**
 * Created by shumeng on 2016/11/29.
 */
public class SetLogo extends Command implements CommandParser{
    private final String COMMAND_NAME = "setLogo";
    private String logoName;
    private Logo logo = null;

    public SetLogo(LogoCommandFactory factory, Logo logo, String [] parameters) {
        super(factory);
        this.logo = logo;
        this.parameters = parameters;
    }

    @Override
    public String getInput() {
        return null;
    }

    @Override
    public boolean checkInput(String var1) {
        return false;
    }

    @Override
    public String getCommandKey() {
        return null;
    }

    @Override
    public Command getNextCommand(ICommandFactory var1, String var2) {
        return null;
    }

    @Override
    public void doExecute() {
        if(parse(parameters)){
            Properties pps = new Properties();
            FileOutputStream out = null;
            try{
                if(StringUtils.equals(system, "windows")){
                    //绝对路径
                    //windows路径
                    //out = new FileOutputStream("D:\\workspace\\deploy\\DtdreamCli\\src\\main\\resources\\logo
                    // .properties");
                    //本地调试时使用
                    String file = Config.class.getClassLoader().getResource("logo.properties").getFile();
                    //System.out.println(file);
                    out = new FileOutputStream(String.valueOf(file));
                }else if(StringUtils.equals(system, "linux")){
                    //linux路径
                    String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                    path = path.substring(0,path.lastIndexOf('/')) + "/config/logo.properties";
                    //System.out.println(path);
                    out = new FileOutputStream(path);
                }else{
                    System.out.println("系统名错误");
                    System.out.println("不支持系统：" + system);
                }

                //从输入流中读取属性列表（键和元素对）
                pps.setProperty("logo", logoName);
                try {
                    pps.store(out, "logo name: ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Logo was set: " + logoName);
                logo.setLogo(logoName);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                CommandRecord.getInstance().popLastCommand();
                try {
                    if(out != null){
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    setLogo 设置控制台应用logo");
        System.out.println("SYNOPSIS");
        System.out.println("    setLogo [options -n] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 修改Dtdream Cli 的控制台logo");
        System.out.println("    -n");
        System.out.println("         --logoName, logo名称。可设为：");
        System.out.println("thomugo:");
        logo.thomugo();
        System.out.println("figlet:");
        logo.figlet();
        System.out.println("puffy:");
        logo.puffy();
        System.out.println("larry3d:");
        logo.larry3d();
        System.out.println("kacky:");
        logo.kacky();
        System.out.println("ivrit:");
        logo.ivrit();
        System.out.println("ascii3d:");
        logo.ascii3d();
        System.out.println("varsity:");
        logo.varsity();
        System.out.println("sweet:");
        logo.sweet();
        System.out.println("swamp_land:");
        logo.swamp_land();
        System.out.println("subZero:");
        logo.subZero();
        System.out.println("soft:");
        logo.soft();
        System.out.println("small_isometric:");
        logo.small_isometric();
        System.out.println("slant:");
        logo.slant();
        System.out.println("ogre:");
        logo.ogre();
        System.out.println("graceful:");
        logo.graceful();
        System.out.println("fire_k:");
        logo.fire_k();
        System.out.println("fire_s:");
        logo.fire_s();
        System.out.println("doom:");
        logo.doom();
        System.out.println("bulbhead:");
        logo.bulbhead();
        System.out.println("big:");
        logo.big();
        System.out.println("diagonal3d:");
        logo.diagonal3d();
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        logoName = parameters[index++];
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                    default:
                        advice(COMMAND_NAME);
                        return false;
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(logoName)){
            System.out.println("参数错误，缺少必要参数，请输入：setLogo -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }else{
            switch (logoName){
                case "thomugo" :
                    logo.setLogo(logoName);
                    break;
                case "figlet" :
                    logo.setLogo(logoName);
                    break;
                case "puffy" :
                    logo.setLogo(logoName);
                    break;
                case "larry3d" :
                    logo.setLogo(logoName);
                    break;
                case "kacky" :
                    logo.setLogo(logoName);
                    break;
                case "ivrit" :
                    logo.setLogo(logoName);
                    break;
                case "ascii3d" :
                    logo.setLogo(logoName);
                    break;
                case "varsity" :
                    logo.setLogo(logoName);
                    break;
                case "sweet" :
                    logo.setLogo(logoName);
                    break;
                case "swamp_land" :
                    logo.setLogo(logoName);
                    break;
                case "sub_zero" :
                    logo.setLogo(logoName);
                    break;
                case "soft" :
                    logo.setLogo(logoName);
                    break;
                case "small_isometric" :
                    logo.setLogo(logoName);
                    break;
                case "slant" :
                    logo.setLogo(logoName);
                    break;
                case "ogre" :
                    logo.setLogo(logoName);
                    break;
                case "graceful" :
                    logo.setLogo(logoName);
                    break;
                case "fire_s" :
                    logo.setLogo(logoName);
                    break;
                case "fire_k" :
                    logo.setLogo(logoName);
                    break;
                case "doom" :
                    logo.setLogo(logoName);
                    break;
                case "bulbhead" :
                    logo.setLogo(logoName);
                    break;
                case "big" :
                    logo.setLogo(logoName);
                    break;
                case "diagonal3d" :
                    logo.setLogo(logoName);
                    break;
                default:
                    System.out.println("不存在名称为：" + logoName + " 的logo, 请输入： setLogo -help 查询帮助");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }
        return true;
    }
}
