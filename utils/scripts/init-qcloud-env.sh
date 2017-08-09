#! /bin/sh -

# 此脚本需要root权限
# 新申请的腾讯机器，可以执行以下命令一键完成初始化
# 首先确保需要的安装包已经上传到ftp了

if [ `whoami` != 'root' ]; then
    echo '非root用户,权限不足！'
    exit -1
fi

# 初始化数据盘
echo -e "n\np\n1\n\n\nwq\n" |fdisk /dev/vdb
mkfs.ext3 /dev/vdb1
if [ ! -d '/data' ]; then
    mkdir /data
fi
mount /dev/vdb1 /data
df -h
echo '/dev/vdb1 /data ext3 defaults 0 0' >> /etc/fstab

# 安装系统工具
yes |yum install gcc gcc-c++ lftp mariadb telnet wget

# 设置各个参数
SOFT_PATH='/data/soft'
INSTALL_PATH='/opt/hoolai'
SERVICE_PATH='/usr/local/services'
LOG_PATH='/data/logs'
TOMCAT_PACKAGE='apache-tomcat-8.5.4.tar.gz'
JDK_PACKAGE='jdk-8u112-linux-x64.rpm'

# 新建路径
mkdir -p $SOFT_PATH
mkdir -p $INSTALL_PATH
mkdir -p $SERVICE_PATH
mkdir -p $LOG_PATH
chmod 777 $LOG_PATH
ln -s $LOG_PATH /logs

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


# 生成download和clean脚本

echo "set ssl:verify-certificate no" >> /etc/lftp.conf

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
ip='ftp-cvmgz00.opencloud.qq.com'
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

