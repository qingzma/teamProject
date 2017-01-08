The source code must be imported into Netbeans IDE. After that, try to run the source code and it will add libraries needed for the project.

After the program has been running, it will display a GUI form with the following buttons:
1. Linear Regression Demo
2. Gaussian Process
3. KNN Demo
4. Non-linear regression Demo
5. RBF Demo
6. Validate

The first five buttons are used for validating and showing plot for each of the fitted value produced based on training dataset by each of the regression algorithm. The last button is used for producing and displaying fitted value of testing dataset. 

The source code offers flexibility in changing parameters used for experiment. It can be changed in App.java. They are as follows
1. Attributes number: change the columns used by Index variable
	int[] index={2,3,4};
2. Cluster number: update the parameter in changeK method
	lr.changeK(5); 
3. Dataset partition: modify the rows number in dc.getRowsBefore, dc.getRowsBetween, and dc.getRowsAfter
	lr=new LinearRegression(dc.getRowsBefore(6607));
        	gps=new GaussianProcessRegression(dc.getRowsBetween(6607, 13214));
        	nl=new PolynomialRegression(dc.getRowsBetween(13214, 19821));
        	rbf=new RBF(dc.getRowsBetween(19821, 26428));
        	knn=new KNNRegression(dc.getRowsBetween(26428, 33035));
        	evaluations=dc.getRowsAfter(33035);
4. Showing plot of fitted value and real value (only in 2 and 3 attributes): unmarking the three line of plot syntax in each of the button
	plt=new Plot();
            plt.showFittingLine(true);
            plt.plot(lr.getClusters(),lr);