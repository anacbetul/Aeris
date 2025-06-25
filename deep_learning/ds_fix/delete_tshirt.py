import pandas as pd
import os

# Ayarlar
csv_path = "merged_dataset.csv"
image_dir = "merged_images"

# CSV'yi oku
df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)

# Fazla T-shirt verilerini seç
tshirt_df = df[df["articleType"] == "T-shirt"]

# Eğer 500'den fazlaysa fazlalıkları al
if len(tshirt_df) > 500:
    excess_tshirts = tshirt_df.sample(n=len(tshirt_df) - 500, random_state=42)

    # Silinecek görsellerin yollarını oluştur
    images_to_delete = excess_tshirts["image_path"].apply(lambda x: os.path.join(image_dir, os.path.basename(x)))

    # Görselleri sil
    deleted_count = 0
    for img_path in images_to_delete:
        if os.path.exists(img_path):
            os.remove(img_path)
            deleted_count += 1

    # DataFrame'den ilgili satırları sil
    df = df.drop(excess_tshirts.index)

    print(f" {deleted_count} adet görsel silindi.")
    print(f" {len(excess_tshirts)} adet T-shirt verisi dataset'ten çıkarıldı.")
else:
    print("ℹ T-shirt verileri zaten 500 veya daha az.")

# (İsteğe bağlı) Yeni temiz dataset'i kaydet
df.to_csv("merged_dataset_cleaned.csv", index=False)
print(" Yeni CSV dosyası: merged_dataset_cleaned.csv olarak kaydedildi.")
