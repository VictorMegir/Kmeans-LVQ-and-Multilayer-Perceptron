data = load ("Kmeanscenters.txt");
data2=load("Kmeanscluster.txt");
plot (data (:,1), data (:,2),'*',data2(:,1),data2(:,2),'+');