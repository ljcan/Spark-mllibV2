import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

data=pd.read_csv("/Users/liujunqiang/Downloads/spark-mllib/spark-mllib-scala/src/main/scala/data/customer_result.csv")
cols=['iterCount','cost']
plt.plot(data[cols])
plt.xlabel("iterCount")
plt.ylabel("cost")
plt.legend
plt.show()

