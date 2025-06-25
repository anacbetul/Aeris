import pandas as pd

# CSV dosyasını yükle
df = pd.read_csv("dataset.csv")

# Aynı image_path'e sahip satırlardan sadece ilkini tut
df_unique = df.drop_duplicates(subset=["image_path"], keep="first")


print(f"Orijinal satır sayısı: {len(df)}")
print(f"Tekrarsız satır sayısı: {len(df_unique)}")
