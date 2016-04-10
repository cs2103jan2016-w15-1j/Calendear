java -jar Calendear.jar < regressiontest.in > output.out
diff regressiontest.out output.out
rm data.txt
rm output.out
