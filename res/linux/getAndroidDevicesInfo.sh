# 获取设备信息并保存到文件，看devicesInfoSample
# 此脚本是为了配合自动化提测平台而编写的
# 5台手机，adb启动了的情况下，0.771s左右完成
# 5太手机，adb没有启动的时候（如重启电脑之后第一次运行时），3.031s左右完成
# jiebo.wang@renren-inc.com 2012-12-02

#TODO adb kill-server之后调用adb devices显示设备，有50%的几率不会显示设备
#TODO adb usb是个好命令
#TODO 多台设备连接，拔下一台设备，再adb kill-server，再adb devices，要么显示之前的设备
# 要么显示被拔下再插上的那个设备
# 总之，就是adb不太靠谱，暂时没有发现什么好的解决办法
# 要是手机一直插着，不去这样干扰，应该是没什么太大问题的，就是怕万一

# serialNumber，设备唯一编号，ID
# availability，设备是否可用，不是指设备已经被占用而不可用，
	# 而是设备能被识别却不能用这一类情况，值为false或者true
# 下面三个参数可以放在网页上显示
# manufacturer:生产商
# model:型号
# release:系统版本号


#从一行字符串中获取设备序列号，tab字符分割的前半部分
#如："emulator-5554	device"，得到"emulator-5554"
function getSerialNumber(){
	serialNumber=$(echo ${1%$'\t'*})
}

#从一行字符串中获取设备状态并转换成可用性（true或false），tab字符分割的后半部分
#如："emulator-5554	device"，得到"device"
#再转换成true或false，"device"为true，其他都是false
#设备状态在mac下一般有三种：device，offline，bootloader
function getAvailability(){
	#alternative command:
	#deviceStatus=$(adb -s $deviceId get-state)
	deviceStatus=$(echo ${1#*$'\t'})
	if [ "$deviceStatus"x = "device"x ]
	then
		availability="true"
	else
		availability="false"
	fi
}

#获取设备生产商
function getManufacturer(){
	manufacturer=$($adb -s $serialNumber shell getprop ro.product.manufacturer | tr -d '\r\n')
}

#获取设备型号
function getModel(){
	model=`$adb -s $serialNumber shell getprop ro.product.model | tr -d '\r\n'`
}

#获取设备的系统版本号
function getRelease(){
	release=$($adb -s $serialNumber shell getprop ro.build.version.release | tr -d '\r\n')
}

#获取设备信息，如果设备可用，才进行设备信息的获取
#获取的信息包括：生产商，型号，系统版本号
#设备不可用时，生产商，型号，系统版本号的值都为空
function getDeviceInfo(){
	manufacturer=""
	model=""
	release=""
	if [ "$availability"x = "true"x ]
	then 
		getManufacturer
		getModel
		getRelease
	fi
}

#保存设备信息的文件名
deviceInfoFileName="devicesInfo"

#删除旧的设备信息文件
if [ -f ./$deviceInfoFileName ]
then
	echo "delete old file"
	rm $deviceInfoFileName
	echo 
fi

#本地adb的绝对路径
adb="./res/macosx/adb"
#10.2.42.82 iMac服务器的adb绝对路径
#adb="/Users/work/bvt-android/android-sdk-macosx/platform-tools/adb"

#重启usb，这句会抛出：error: more than one device and emulator，暂时去掉先
#$adb -d usb
#重启adb
#不kill了
#$adb kill-server
$adb start-server

#处理adb devices命令输出的字符串
currentDevicesInfoRaw=$($adb devices)
#进行字符串的截取，特别是当adb没有启动的时候会显示一些多余的控制台输出，直接砍掉，只保留设备信息的字符串
currentDevicesInfo=${currentDevicesInfoRaw#*List of devices attached}
echo -----------currentDevicesInfo--start--------------
echo "$currentDevicesInfo"
echo -----------currentDevicesInfo---end---------------
numberOfLines=$(echo "$currentDevicesInfo" | wc -l) 
#按行来进行设备信息的获取和保存
for((i=2;i<=$numberOfLines;i++))
do 
	deviceInfo=$(echo "$currentDevicesInfo" | awk NR==$i)
	getSerialNumber "$deviceInfo"
	getAvailability "$deviceInfo"
	getDeviceInfo
	record="serialNumber=$serialNumber,availability=$availability,manufacturer=$manufacturer,model=$model,release=$release"
	echo ${record} | tee -a $deviceInfoFileName 
done
echo "\ndone"