import pandas as pd

# Dosyaları oku
df1 = pd.read_csv("styles_filtered_minimal.csv")
df2 = pd.read_csv("clothes_labels_tr.csv")

# Birleştir
df_merged = pd.concat([df1, df2]).reset_index(drop=True)

# İsteğe bağlı: Aynı id'den varsa kaldırmak istersen
# df_merged = df_merged.drop_duplicates(subset='id')

# Yeni dosyayı kaydet
df_merged.to_csv("styles_filtered_merged.csv", index=False)

print("Birleştirilmiş dosya kaydedildi: styles_filtered_merged.csv")
