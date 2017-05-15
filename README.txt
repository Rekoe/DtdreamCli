Dtdream Cli :

   version: 1.0.0
   Dtdream cli是数梦工厂提供的操作阿里云的命令行工具,是阿里云资产管理和配置的统一工具, 您只需要简单的安装和配置, 即可通过命令行方式同时管理多个阿里云产品和服务,
   简单快捷, 是您上云的好帮手.

使用帮助：
   安装前的准备：
     本软件目前对linux系统有着良好的支持。
     1. 请确保软件的安装过程是在linux操作系统下进行的。
     2. 请确保系统已经完成JDK7及对应环境变量的安装和配置。
     3. 请确保系统已经安装unzip解压缩工具
     4. 请确保网络畅通，能正常访问到云账号下的资源。

   安装过程：
     1. 将软件包解压到指定的安装目录，例如：unzip DtdreamCli-1.0.0-beta.zip -d destination(目的路径)
     2. 进入软件根目录，给软件的安装、执行、卸载脚本添加可执行权限：
        cd destination/DtdreamCli-1.0.0/
        chmod +x *.sh
     3. 执行脚本ins.sh,安装DtdreamCli, ./ins.sh

   使用过程：
     Dtdream Cli有两种模式：命令模式，交互模式。
     1. 交互模式：直接在linux终端输入dtdream并回车，即可进入软件交互模式，在交互模式中直接输入对应命令即可。
        命令格式：dtdream [command] [options]
        帮助：在command 或 options字段输入-help 或--help 即可查询当前命令的使用帮助。
        退出：输入 exit 或 quit 命令即可退出交互模式
     2. 命令模式：直接在linux终端输入：dtdream [command] [options] 回车，即可得到当前命令的执行结果。

   使用技巧：
     本产品提供多种logo供您选择，如需替换则执行：dtdream setLogo -n [logoName] 命令，即可替换产品logo。
     查询更多logo, 请输入：dtdream logo -help 命令。

注意事项：
    本产品是通过云账号的akID,和akSecret来授权错作该云账号下的资源的。可通过修改：软件根目录/conf/config.properties
    文件中的对应配置来操作对应的资源。