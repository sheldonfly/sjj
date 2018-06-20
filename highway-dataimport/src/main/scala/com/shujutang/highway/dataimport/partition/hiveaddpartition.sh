oldifs=$IFS
sed -i "/^$/d" ./hivepartition.txt
cat ./hivepartition.txt | while read hivea
do
a=`echo $hivea`
IFS= 
for hiveb in `echo $a`
do
hive -e $hiveb
aa=`echo $?`
if [ $aa != 0 ];then
echo "$hiveb" >> ./hiveaddpartition-fail.log
else
echo "$hiveb" >> ./hiveaddpartition-ok.log
fi
done
IFS=$oldifs
done