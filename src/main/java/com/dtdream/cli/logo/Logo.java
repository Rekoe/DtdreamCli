package com.dtdream.cli.logo;

import com.dtdream.cli.util.Config;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shumeng on 2016/11/28.
 */
public class Logo {

    public static String logo = "thomugo";

    public Logo(){
    }

    public Logo(String system){
        Properties pps = new Properties();
        InputStream in = null;
        try{
            if(StringUtils.equals(system, "windows")){
                //绝对路径
                //windows路径
                //in = new FileInputStream("D:\\workspace\\deploy\\DtdreamCli\\src\\main\\resources\\logo.properties");
                //本地调试时使用
                in = Config.class.getClassLoader().getResourceAsStream("logo.properties");
            }else if(StringUtils.equals(system, "linux")){
                //linux路径
                String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                path = path.substring(0,path.lastIndexOf('/')) + "/config/logo.properties";
                //System.out.println(path);
                in = new FileInputStream(path);
            }else{
                System.out.println("系统名错误");
                System.out.println("不支持系统：" + system);
            }

            try {
                pps.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //从输入流中读取属性列表（键和元素对）
            logo = pps.getProperty("logo");
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void show(){
        switch (logo){
            case "thomugo" :
                thomugo();
                break;
            case "figlet" :
                figlet();
                break;
            case "puffy" :
                puffy();
                break;
            case "larry3d" :
                larry3d();
                break;
            case "kacky" :
                kacky();
                break;
            case "ivrit" :
                ivrit();
                break;
            case "ascii3d" :
                ascii3d();
                break;
            case "varsity" :
                varsity();
                break;
            case "sweet" :
                sweet();
                break;
            case "swamp_land" :
                swamp_land();
                break;
            case "sub_zero" :
                subZero();
                break;
            case "soft" :
                soft();
                break;
            case "small_isometric" :
                small_isometric();
                break;
            case "slant" :
                slant();
                break;
            case "ogre" :
                ogre();
                break;
            case "graceful" :
                graceful();
                break;
            case "fire_s" :
                fire_s();
                break;
            case "fire_k" :
                fire_k();
                break;
            case "doom" :
                doom();
                break;
            case "bulbhead" :
                bulbhead();
                break;
            case "big" :
                big();
                break;
            case "diagonal3d" :
                diagonal3d();
                break;
            default:
                thomugo();
        }
    }

    public void thomugo(){
        System.out.println();
        System.out.println(" _ _ _     __        _");
        System.out.println("|  __ \\  _|  |_   __| | _ __  ___   __ _  _ __ ___");
        System.out.println("| |  \\ ||_  _ _| / _' || '_/ / _ \\ / _` || '_ ` _ \\");
        System.out.println("| |__/ |  | |_  | (_| || |  |  __/| (_| || | | | | |");
        System.out.println("|_ _ _/   |_ _/  \\____||_|   \\___| \\__,_||_| |_| |_|");
        System.out.println();
    }

    public void figlet(){
        System.out.println();
        System.out.println("     _ _      _");
        System.out.println("  __| | |_ __| |_ __ ___  __ _ _ __ ___");
        System.out.println(" / _` | __/ _` | '__/ _ \\/ _` | '_ ` _ \\");
        System.out.println("| (_| | || (_| | | |  __/  (_|| | | | | |");
        System.out.println(" \\__,_|\\__\\__,_|_|  \\___|\\__,_|_| |_| |_|");
        System.out.println();
    }

    public void puffy(){
        System.out.println();
        System.out.println(" ___    _        _");
        System.out.println("(  _`\\ ( )_     ( )");
        System.out.println("| | ) || ,_)   _| | _ __   __     _ _   ___ ___");
        System.out.println("| | | )| |   /'_` |( '__)/'__`\\ /'_` )/' _ ` _ `\\");
        System.out.println("| |_) || |_ ( (_| || |  (  ___/( (_| || ( ) ( ) |");
        System.out.println("(____/'`\\__)`\\__,_)(_)  `\\____)`\\__,_)(_) (_) (_)");
        System.out.println();
    }

    public void larry3d(){
        System.out.println();
        System.out.println(" ____    __       __");
        System.out.println("/\\  _`\\ /\\ \\__   /\\ \\");
        System.out.println("\\ \\ \\/\\ \\ \\ ,_\\  \\_\\ \\  _ __    __     __      ___ ___");
        System.out.println(" \\ \\ \\ \\ \\ \\ \\/  /'_` \\/\\`'__\\/'__`\\ /'__`\\  /' __` __`\\");
        System.out.println("  \\ \\ \\_\\ \\ \\ \\_/\\ \\L\\ \\ \\ \\//\\  __//\\ \\L\\.\\_/\\ \\/\\ \\/\\ \\");
        System.out.println("   \\ \\____/\\ \\__\\ \\___,_\\ \\_\\\\ \\____\\ \\__/.\\_\\ \\_\\ \\_\\ \\_\\");
        System.out.println("    \\/___/  \\/__/\\/__,_ /\\/_/ \\/____/\\/__/\\/_/\\/_/\\/_/\\/_/");
        System.out.println();
    }

    public void kacky(){
        System.out.println();
        System.out.println(" ______     ________   ______     ______      _____     ____       __    __");
        System.out.println("(_  __ \\   (___  ___) (_  __ \\   (   __ \\    / ___/    (    )      \\ \\  / /");
        System.out.println("  ) ) \\ \\      ) )      ) ) \\ \\   ) (__) )  ( (__      / /\\ \\      () \\/ ()");
        System.out.println(" ( (   ) )    ( (      ( (   ) ) (    __/    ) __)    ( (__) )     / _  _ \\");
        System.out.println("  ) )  ) )     ) )      ) )  ) )  ) \\ \\  _  ( (        )    (     / / \\/ \\ \\");
        System.out.println(" / /__/ /     ( (      / /__/ /  ( ( \\ \\_))  \\ \\___   /  /\\  \\   /_/      \\_\\");
        System.out.println("(______/      /__\\    (______/    )_) \\__/    \\____\\ /__(  )__\\ (/          \\)");
        System.out.println();
    }

    public void ivrit(){
        System.out.println();
        System.out.println("  ____  _      _");
        System.out.println(" |  _ \\| |_ __| |_ __ ___  __ _ _ __ ___");
        System.out.println(" | | | | __/ _` | '__/ _ \\/ _` | '_ ` _ \\");
        System.out.println(" | |_| | || (_| | | |  __/ (_| | | | | | |");
        System.out.println(" |____/ \\__\\__,_|_|  \\___|\\__,_|_| |_| |_|");
        System.out.println();
    }

    public void ascii3d(){
        System.out.println();
        System.out.println(" ________  _________  ________  ________  _______   ________  _____ ______");
        System.out.println("|\\   ___ \\|\\___   ___\\\\   ___ \\|\\   __  \\|\\  ___ \\ |\\   __  \\|\\   _ \\  _   " +
                "\\");
        System.out.println("\\ \\  \\_|\\ \\|___ \\  \\_\\ \\  \\_|\\ \\ \\  \\|\\  \\ \\   __/|\\ \\  \\|\\  \\ \\  " +
                "\\\\\\__\\ \\  \\");
        System.out.println(" \\ \\  \\ \\\\ \\   \\ \\  \\ \\ \\  \\ \\\\ \\ \\   _  _\\ \\  \\_|/_\\ \\   __  \\ \\ " +
                " \\\\|__| \\  \\");
        System.out.println("  \\ \\  \\_\\\\ \\   \\ \\  \\ \\ \\  \\_\\\\ \\ \\  \\\\  \\\\ \\  \\_|\\ \\ \\  \\ \\ " +
                " \\ \\  \\    \\ \\  \\");
        System.out.println("   \\ \\_______\\   \\ \\__\\ \\ \\_______\\ \\__\\\\ _\\\\ \\_______\\ \\__\\ \\__\\ " +
                "\\__\\    \\ \\__\\");
        System.out.println("    \\|_______|    \\|__|  \\|_______|\\|__|\\|__|\\|_______|\\|__|\\|__|\\|__|     \\|__|");
        System.out.println();
    }

    public void varsity(){
        System.out.println();
        System.out.println(" ______     _         __");
        System.out.println("|_   _ `.  / |_      |  ]");
        System.out.println("  | | `. \\`| |-' .--.| |  _ .--.  .---.  ,--.   _ .--..--.");
        System.out.println("  | |  | | | | / /'`\\' | [ `/'`\\]/ /__\\\\`'_\\ : [ `.-. .-. |");
        System.out.println(" _| |_.' / | |,| \\__/  |  | |    | \\__.,// | |, | | | | | |");
        System.out.println("|______.'  \\__/ '.__.;__][___]    '.__.'\\'-;__/[___||__||__]");
        System.out.println();
    }

    public void sweet(){
        System.out.println();
        System.out.println("     ___   ___           ___");
        System.out.println("    (   ) (   )         (   )");
        System.out.println("  .-.| |   | |_       .-.| |   ___ .-.      .--.     .---.   ___ .-. .-.");
        System.out.println(" /   \\ |  (   __)    /   \\ |  (   )   \\    /    \\   / .-, \\ (   )   '   \\");
        System.out.println("|  .-. |   | |      |  .-. |   | ' .-. ;  |  .-. ; (__) ; |  |  .-.  .-. ;");
        System.out.println("| |  | |   | | ___  | |  | |   |  / (___) |  | | |   .'`  |  | |  | |  | |");
        System.out.println("| |  | |   | |(   ) | |  | |   | |        |  |/  |  / .'| |  | |  | |  | |");
        System.out.println("| |  | |   | | | |  | |  | |   | |        |  ' _.' | /  | |  | |  | |  | |");
        System.out.println("| '  | |   | ' | |  | '  | |   | |        |  .'.-. ; |  ; |  | |  | |  | |");
        System.out.println("' `-'  /   ' `-' ;  ' `-'  /   | |        '  `-' / ' `-'  |  | |  | |  | |");
        System.out.println(" `.__,'     `.__.    `.__,'   (___)        `.__.'  `.__.'_. (___)(___)(___)");
        System.out.println();
    }

    public void swamp_land(){
        System.out.println();
        System.out.println(" ______   _________  ______   ______    ______   ________   ___ __ __");
        System.out.println("/_____/\\ /________/\\/_____/\\ /_____/\\  /_____/\\ /_______/\\ /__//_//_/\\");
        System.out.println("\\:::_ \\ \\\\__.::.__\\/\\:::_ \\ \\\\:::_ \\ \\ \\::::_\\/_\\::: _  \\ \\\\::\\| \\| \\" +
                " \\");
        System.out.println(" \\:\\ \\ \\ \\  \\::\\ \\   \\:\\ \\ \\ \\\\:(_) ) )_\\:\\/___/\\\\::(_)  \\ \\\\:.     " +
                " \\ \\");
        System.out.println("  \\:\\ \\ \\ \\  \\::\\ \\   \\:\\ \\ \\ \\\\: __ `\\ \\\\::___\\/_\\:: __  \\ \\\\:" +
                ".\\-/\\  \\ \\");
        System.out.println("   \\:\\/.:| |  \\::\\ \\   \\:\\/.:| |\\ \\ `\\ \\ \\\\:\\____/\\\\:.\\ \\  \\ \\\\. \\ " +
                " \\  \\ \\");
        System.out.println("    \\____/_/   \\__\\/    \\____/_/ \\_\\/ \\_\\/ \\_____\\/ \\__\\/\\__\\/ \\__\\/ " +
                "\\__\\/");
        System.out.println();
    }

    public void subZero(){
        System.out.println();
        System.out.println(" _____     ______   _____     ______     ______     ______     __    __");
        System.out.println("/\\  __-.  /\\__  _\\ /\\  __-.  /\\  == \\   /\\  ___\\   /\\  __ \\   /\\ \"-./  \\");
        System.out.println("\\ \\ \\/\\ \\ \\/_/\\ \\/ \\ \\ \\/\\ \\ \\ \\  __<   \\ \\  __\\   \\ \\  __ \\  \\ \\ " +
                "\\-./\\ \\");
        System.out.println(" \\ \\____-    \\ \\_\\  \\ \\____-  \\ \\_\\ \\_\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ " +
                "\\_\\ \\ \\_\\");
        System.out.println("  \\/____/     \\/_/   \\/____/   \\/_/ /_/   \\/_____/   \\/_/\\/_/   \\/_/  \\/_/");
        System.out.println();
    }

    public void soft(){
        System.out.println();
        System.out.println(",------.    ,--.     ,--.");
        System.out.println("|  .-.  \\ ,-'  '-. ,-|  |,--.--. ,---.  ,--,--.,--,--,--.");
        System.out.println("|  |  \\  :'-.  .-'' .-. ||  .--'| .-. :' ,-.  ||        |");
        System.out.println("|  '--'  /  |  |  \\ `-' ||  |   \\   --.\\ '-'  ||  |  |  |");
        System.out.println("`-------'   `--'   `---' `--'    `----' `--`--'`--`--`--'");
        System.out.println();
    }

    public void small_isometric(){
        System.out.println();
        System.out.println("    ___       ___       ___       ___       ___       ___       ___");
        System.out.println("   /\\  \\     /\\  \\     /\\  \\     /\\  \\     /\\  \\     /\\  \\     /\\__\\");
        System.out.println("  /::\\  \\    \\:\\  \\   /::\\  \\   /::\\  \\   /::\\  \\   /::\\  \\   /::L_L_");
        System.out.println(" /:/\\:\\__\\   /::\\__\\ /:/\\:\\__\\ /::\\:\\__\\ /::\\:\\__\\ /::\\:\\__\\ /:/L:\\__\\");
        System.out.println(" \\:\\/:/  /  /:/\\/__/ \\:\\/:/  / \\;:::/  / \\:\\:\\/  / \\/\\::/  / \\/_/:/  /");
        System.out.println("  \\::/  /   \\/__/     \\::/  /   |:\\/__/   \\:\\/  /    /:/  /    /:/  /");
        System.out.println("   \\/__/               \\/__/     \\|__|     \\/__/     \\/__/     \\/__/");
        System.out.println();
    }

    public void slant(){
        System.out.println();
        System.out.println("    ____  __      __");
        System.out.println("   / __ \\/ /_____/ /_______  ____ _____ ___");
        System.out.println("  / / / / __/ __  / ___/ _ \\/ __ `/ __ `__ \\");
        System.out.println(" / /_/ / /_/ /_/ / /  /  __/ /_/ / / / / / /");
        System.out.println("/_____/\\__/\\__,_/_/   \\___/\\__,_/_/ /_/ /_/");
        System.out.println();
    }

    public void ogre(){
        System.out.println();
        System.out.println("    ___ _      _");
        System.out.println("   /   \\ |_ __| |_ __ ___  __ _ _ __ ___");
        System.out.println("  / /\\ / __/ _` | '__/ _ \\/ _` | '_ ` _ \\");
        System.out.println(" / /_//| || (_| | | |  __/ (_| | | | | | |");
        System.out.println("/___,'  \\__\\__,_|_|  \\___|\\__,_|_| |_| |_|");
        System.out.println();
    }

    public void graceful(){
        System.out.println();
        System.out.println(" ____  ____  ____  ____  ____   __   _  _");
        System.out.println("(    \\(_  _)(    \\(  _ \\(  __) / _\\ ( \\/ )");
        System.out.println(" ) D (  )(   ) D ( )   / ) _) /    \\/ \\/ \\");
        System.out.println("(____/ (__) (____/(__\\_)(____)\\_/\\_/\\_)(_/");
        System.out.println();
    }

    public void fire_s(){
        System.out.println();
        System.out.println(" (");
        System.out.println(" )\\ )     ) (");
        System.out.println("(()/(  ( /( )\\ ) (     (    )    )");
        System.out.println(" /(_)) )\\()|()/( )(   ))\\( /(   (");
        System.out.println("(_))_ (_))/ ((_)|()\\ /((_)(_))  )\\  '");
        System.out.println(" |   \\| |_  _| | ((_|_))((_)_ _((_))");
        System.out.println(" | |) |  _/ _` || '_/ -_) _` | '  \\()");
        System.out.println(" |___/ \\__\\__,_||_| \\___\\__,_|_|_|_|");
        System.out.println();
    }

    public void fire_k(){
        System.out.println();
        System.out.println("(");
        System.out.println(" )\\ )      )  (");
        System.out.println("(()/(   ( /(  )\\ )  (      (     )     )");
        System.out.println(" /(_))  )\\())(()/(  )(    ))\\ ( /(    (");
        System.out.println("(_))_  (_))/  ((_))(()\\  /((_))(_))   )\\  '");
        System.out.println(" |   \\ | |_   _| |  ((_)(_)) ((_)_  _((_))");
        System.out.println(" | |) ||  _|/ _` | | '_|/ -_)/ _` || '  \\()");
        System.out.println(" |___/  \\__|\\__,_| |_|  \\___|\\__,_||_|_|_|");
        System.out.println();
    }

    public void doom(){
        System.out.println();
        System.out.println("______ _      _");
        System.out.println("|  _  \\ |    | |");
        System.out.println("| | | | |_ __| |_ __ ___  __ _ _ __ ___");
        System.out.println("| | | | __/ _` | '__/ _ \\/ _` | '_ ` _ \\");
        System.out.println("| |/ /| || (_| | | |  __/ (_| | | | | | |");
        System.out.println("|___/  \\__\\__,_|_|  \\___|\\__,_|_| |_| |_|");
        System.out.println();
    }

    public void bulbhead(){
        System.out.println();
        System.out.println(" ____  ____  ____  ____  ____    __    __  __");
        System.out.println("(  _ \\(_  _)(  _ \\(  _ \\( ___)  /__\\  (  \\/  )");
        System.out.println(" )(_) ) )(   )(_) ))   / )__)  /(__)\\  )    (");
        System.out.println("(____/ (__) (____/(_)\\_)(____)(__)(__)(_/\\/\\_)");
        System.out.println();
    }

    public void big(){
        System.out.println();
        System.out.println("  _____  _      _");
        System.out.println(" |  __ \\| |    | |");
        System.out.println(" | |  | | |_ __| |_ __ ___  __ _ _ __ ___");
        System.out.println(" | |  | | __/ _` | '__/ _ \\/ _` | '_ ` _ \\");
        System.out.println(" | |__| | || (_| | | |  __/ (_| | | | | | |");
        System.out.println(" |_____/ \\__\\__,_|_|  \\___|\\__,_|_| |_| |_|");
        System.out.println();
    }

    public void diagonal3d(){
        System.out.println();
        System.out.println("    ,---,        ___                                                       ____");
        System.out.println("  .'  .' `\\    ,--.'|_        ,---,                                      ,'  , `.");
        System.out.println(",---.'     \\   |  | :,'     ,---.'|  __  ,-.                          ,-+-,.' _ |");
        System.out.println("|   |  .`\\  |  :  : ' :     |   | :,' ,'/ /|                       ,-+-. ;   , ||");
        System.out.println(":   : |  '  |.;__,'  /      |   | |'  | |' | ,---.     ,--.--.    ,--.'|'   |  ||");
        System.out.println("|   ' '  ;  :|  |   |     ,--.__| ||  |   ,'/     \\   /       \\  |   |  ,', |  |,");
        System.out.println("'   | ;  .  |:__,'| :    /   ,'   |'  :  / /    /  | .--.  .-. | |   | /  | |--'");
        System.out.println("|   | :  |  '  '  : |__ .   '  /  ||  | ' .    ' / |  \\__\\/: . . |   : |  | ,");
        System.out.println("'   : | /  ;   |  | '.'|'   ; |:  |;  : | '   ;   /|  ,\" .--.; | |   : |  |/");
        System.out.println("|   | '` ,/    ;  :    ;|   | '/  '|  , ; '   |  / | /  /  ,.  | |   | |`-'");
        System.out.println(";   :  .'      |  ,   / |   :    :| ---'  |   :    |;  :   .'   \\|   ;/");
        System.out.println("|   ,.'         ---`-'   \\   \\  /          \\   \\  / |  ,     .-./'---'");
        System.out.println("'---'                     `----'            `----'   `--`---'");
        System.out.println();
    }

}
