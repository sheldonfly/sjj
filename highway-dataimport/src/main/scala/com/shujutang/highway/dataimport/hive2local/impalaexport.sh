oldifs=$IFS
sed -i "/^$/d" /home/catsic/shujutang/sh/test/hivetolocalcmd.txt
cat /home/catsic/shujutang/sh/test/hivetolocalcmd.txt | while read hivea
do
a=`echo $hivea`
IFS= 
b=`echo $a | awk -F '>>' '{print $1}'`
c=`echo $a | awk -F '>>' '{print $2}'`
for hiveb in `echo $b`
do
IFS=$oldifs
echo "impala-shell -B --print_header --output_delimiter='\t' -q $hiveb > $c"
impala-shell -B --print_header --output_delimiter='\t' -q "$hiveb" > $c
aa=`echo $?`
if [ $aa != 0 ];then
echo "$hiveb >> $c" >> /home/catsic/shujutang/sh/test/hiveexport-fail.log
else
echo "$hiveb >> $c" >> /home/catsic/shujutang/sh/test/hiveexport-ok.log
fi
done
IFS=$oldifs
done
