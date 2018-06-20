oldifs=$IFS
sed -i "/^$/d" ./hivetolocalcmd.txt
cat /home/catsic/shujutang/sh/C/hivetolocalcmd.txt | while read hivea
do
a=`echo $hivea`
IFS= 
b=`echo $a | awk -F '>>' '{print $1}'`
c=`echo $a | awk -F '>>' '{print $2}'`
for hiveb in `echo $b`
do
IFS=$oldifs
echo "hive -e "$hiveb" > $c"
hive -e "$hiveb" > $c
aa=`echo $?`
if [ $aa != 0 ];then
echo "$hiveb >> $c" >> /home/catsic/shujutang/sh/C/hiveexport-fail.log
else
echo "$hiveb >> $c" >> /home/catsic/shujutang/sh/C/hiveexport-ok.log
fi
done
IFS=$oldifs
done
