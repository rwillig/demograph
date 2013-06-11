# Introduction to demograph

TODO: write [great documentation](http://jacobian.org/writing/great-documentation/what-to-write/)

steps to exercise what's there so far:
	(load-file "src/demograph/core.clj")
	(def master-list (demograph.core/read-master-file "data/master.csv"))
	(demograph.core/filter-data-file "data/in.csv" "data/out.csv" master-list)
