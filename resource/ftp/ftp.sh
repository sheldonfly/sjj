a=`echo $1`
b=`echo $2`
if [ ! -n "$a" ];then
echo "参数错误"
else
lftp ftpdata:Poiuytrewq1@124.207.79.164 << EOF
$1 $2 $3 $4 $5 $6
exit
EOF
fi
exit 0
