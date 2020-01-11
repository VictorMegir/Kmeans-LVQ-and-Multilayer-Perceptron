data = load ("LVQcenters.txt");
data2=load("LVQcluster.txt");
plot (data (:,1), data (:,2),'*',data2(:,1),data2(:,2),'+');
