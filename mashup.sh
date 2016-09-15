find /usr/  -name "*.[cpp|c|hh|h]" -print0 | while IFS= read -r -d $'\0' line; do
    hadoop fs -appendToFile "$line" /user/wvervale/sourcefile.txt
done
