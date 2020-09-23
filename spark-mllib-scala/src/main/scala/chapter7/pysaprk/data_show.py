import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

df=pd.read_csv('/Users/liujunqiang/Downloads/spark-mllib/spark-mllib-scala/src/main/scala/data/hour.csv',header=0)
df.head()
sns.set(style='whitegrid',context='notebook')
cols= [ 'season' , 'yr','temp','atemp','hum' , 'windspeed' , 'cnt']
sns.pairplot(df[cols],size=2.5)
plt.show()

