echo Testing...
java -jar Calendear.jar < regressiontest.in > output.out
diff -b regressiontest.out output.out
rm data.txt
rm output.out
