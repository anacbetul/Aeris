import pandas as pd
import os

# 1. CSV'leri oku
df1 = pd.read_csv("clothes_labels_tr.csv")
df2 = pd.read_csv("styles_filtered_minimal.csv")

# 2. Her birine klasör bilgisini ekleyerek path oluştur
df1["image_path"] = df1["id"].apply(lambda x: os.path.join("Clothes_Dataset", f"{x}.jpg"))
df2["image_path"] = df2["id"].apply(lambda x: os.path.join("images_filtered", f"{x}.jpg"))

# 3. İki veri kümesini birleştir
merged_df = pd.concat([df1, df2], ignore_index=True)

# 4. Sonucu kontrol et
print(merged_df.head())

# 5. İstersen kaydet
merged_df = merged_df[merged_df["image_path"].apply(os.path.exists)]

