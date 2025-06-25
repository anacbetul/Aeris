import os
import pandas as pd

# Ayarlar
csv_path = "merged_dataset_cleaned.csv"
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

# Eksik olan görselleri sil
for missing_image in missing_in_folder:
    image_path = os.path.join(image_dir, missing_image)
    if os.path.exists(image_path):
        os.remove(image_path)
        print(f"{missing_image} silindi.")
    else:
        print(f"{missing_image} bulunamadı.")
