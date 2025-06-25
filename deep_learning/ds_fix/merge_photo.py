import os
import shutil
import pandas as pd

# Ortak klasör oluştur
output_folder = "merged_images"
os.makedirs(output_folder, exist_ok=True)

# CSV'leri yükle
df1 = pd.read_csv("clothes_labels_tr.csv")
df2 = pd.read_csv("styles_filtered_minimal.csv")

# Görselleri kopyala
for idx in df1["id"]:
    src = os.path.join("Clothes_Dataset", f"{idx}.jpg")
    dst = os.path.join(output_folder, f"{idx}.jpg")
    if os.path.exists(src):
        shutil.copy(src, dst)

for idx in df2["id"]:
    src = os.path.join("images_filtered", f"{idx}.jpg")
    dst = os.path.join(output_folder, f"{idx}.jpg")
    if os.path.exists(src):
        shutil.copy(src, dst)
