#! /bin/sh -

# 此脚本需要root权限
# 新申请的腾讯机器，可以执行以下命令一键完成初始化
# 首先确保需要的安装包已经上传到ftp了

if [ `whoami` != 'root' ]; then
    echo '非root用户,权限不足！'
    exit -1
fi

# 设置各个参数
SOFT_PATH='/data/soft'
INSTALL_PATH='/opt/hoolai'
SERVICE_PATH='/usr/local/services'
TOMCAT_PACKAGE='apache-tomcat-8.5.4.tar.gz'
JDK_PACKAGE='jdk-8u112-linux-x64.rpm'

# 新建路径
mkdir -p $SOFT_PATH
mkdir -p $INSTALL_PATH

# 修改profile，并且显示内容，防止重复添加
# 如已重复添加，需要手工删除重复内容
echo "
export LANG=zh_CN.UTF-8
export LC_ALL=zh_CN.UTF-8
export PATH=\$PATH:$INSTALL_PATH
export JAVA_HOME=/usr/java/jdk1.8.0_112
" >> /etc/profile
. /etc/profile

echo '/etc/profile'
echo '--------------------------------------------------------'
cat /etc/profile
echo '--------------------------------------------------------'

# 更新yum源
cd $SOFT_PATH
wget -q http://mirrors.tencentyun.com/install/softinst.sh && sh softinst.sh
wget -q http://mirrors.tencentyun.com/install/yum_deploy/fix_tlinux_yum.sh && sh fix_tlinux_yum.sh

# 安装系统工具
yum install gcc gcc-c++

# 生成download和clean脚本
echo "#! /bin/bash -

if [ \$# -lt 1 -o \$# -gt 2 ]; then
        echo 'Usage: download filename [dst_path]'
        exit -1
fi

filename=\$1
dst_path=.
if [ \$# -eq 2 ]; then
        dst_path=\$2
fi

user='app100632434'
passwd='xiangxing!@#'
ip='cvmftp.tencentyun.com'
port='53000'

lftp -u\$user,\$passwd \$ip:\$port -e 'mget '\$filename' -O '\$dst_path'/; quit'
" >$INSTALL_PATH/download
chmod +x $INSTALL_PATH/download

echo "#! /bin/bash -

arr=\$@

for var in \${arr[@]};
do
    for i in \`ls \$var |grep -v 'log\$' |grep -v 'gz\$'\`;
    do
        gzip \$var/\$i
    done
    find \$var -mtime +15 -exec rm -f {} \;
done
" >$INSTALL_PATH/clean
chmod +x $INSTALL_PATH/clean

# 下载所需要的软件包
# su - user_00 <<EOF
cd $SOFT_PATH
download $JDK_PACKAGE
download $TOMCAT_PACKAGE
# exit;
# EOF

# 安装需要的软件包
rpm -hiv $JDK_PACKAGE
tar xvzf $TOMCAT_PACKAGE -C $SERVICE_PATH

echo 'All is Done!'

