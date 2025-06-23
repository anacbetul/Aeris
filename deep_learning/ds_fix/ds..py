import os
import pandas as pd

# Ayarlar
csv_path = "dataset.csv"
image_dir = "merged_images"

# CSV'deki verileri oku
df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)

# CSV'deki görsel dosyalarının adları
csv_image_filenames = df["image_path"].apply(lambda x: os.path.basename(str(x))).tolist()

# Klasördeki görsellerin adları
folder_image_filenames = os.listdir(image_dir)

# Set farkları
csv_set = set(csv_image_filenames)
folder_set = set(folder_image_filenames)

missing_in_folder = csv_set - folder_set
extra_in_folder = folder_set - csv_set

# Sonuçları yazdır
print("CSV'deki görsel sayısı:", len(csv_set))
print("Klasördeki görsel sayısı:", len(folder_set))
print("Klasörde eksik olanlar (CSV'de var ama dosya yok):", len(missing_in_folder))
print("CSV'de olmayan ama klasörde fazla olan görseller:", len(extra_in_folder))
import os
import pandas as pd

# Ayarlar
csv_path = "dataset.csv"
image_dir = "merged_images"

# CSV'yi oku
df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)

# Görsel adlarını çıkart
df["filename"] = df["image_path"].apply(lambda x: os.path.basename(str(x)))
csv_image_filenames = set(df["filename"].tolist())
folder_image_filenames = set(os.listdir(image_dir))

# Eksik olanlar (CSV'de var, klasörde yok)
missing_in_folder = csv_image_filenames - folder_image_filenames

# Bu satırları DataFrame'den çıkar
filtered_df = df[~df["filename"].isin(missing_in_folder)].drop(columns=["filename"])

# Güncellenmiş CSV'yi yeniden yaz
filtered_df.to_csv(csv_path, index=False)

print(f"{len(missing_in_folder)} satır silindi. Güncellenmiş dataset.csv kaydedildi.")
# import os
# import pandas as pd

# # Ayarlar
# csv_path = "dataset.csv"
# image_dir = "merged_images"

# # CSV'deki görsel adlarını al
# df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)
# csv_image_filenames = set(df["image_path"].apply(lambda x: os.path.basename(str(x))).tolist())

# # Klasördeki görsellerin adları
# folder_image_filenames = set(os.listdir(image_dir))

# # CSV'de olmayan ama klasörde olan (fazlalık) dosyalar
# extra_in_folder = folder_image_filenames - csv_image_filenames

# # Bu dosyaları sil
# for filename in extra_in_folder:
#     file_path = os.path.join(image_dir, filename)
#     if os.path.exists(file_path):
#         os.remove(file_path)
#         print(f"Silindi: {filename}")
#     else:
#         print(f"Zaten yoktu: {filename}")
