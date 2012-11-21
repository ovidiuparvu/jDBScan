# Any comments can be specified using this notation
set terminal svg
set output "errorbars.svg"
# The key refers to the legend
set key off
set xrange [-2:5500]
set yrange [-50000:25000000000]
set ylabel "Amount of ERKPP"
set xlabel "Time"
set title "Clusters in EFACTS project using errorbars"
plot "clusters_errorbars.txt" using 1:4:2:3 with yerrorlines linecolor rgbcolor "#61bfa0", \
	 "clusters_errorbars.txt" using 1:7:5:6 with yerrorlines linecolor rgbcolor "#8a9ba", \
	 "clusters_errorbars.txt" using 1:10:8:9 with yerrorlines linecolor rgbcolor "#9745", \
	 "clusters_errorbars.txt" using 1:13:11:12 with yerrorlines linecolor rgbcolor "#391c6e"
pause -1 "Hit any key to continue"
