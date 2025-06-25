import os
import pandas as pd

# Dosya ve klasör yolları
csv_path = "dataset.csv"  # CSV dosyanın yolu
image_dir = "merged_images"              # Görsellerin bulunduğu klasör
output_path = "cleaned_dataset.csv"      # Temizlenen yeni CSV dosyasının adı

# CSV dosyasını yükle
df = pd.read_csv(csv_path)

# image_path'ten sadece dosya adını al (klasörde karşılaştırmak için)
df["image_filename"] = df["image_path"].apply(lambda x: os.path.basename(str(x)).strip())

# merged_images klasöründeki tüm dosyaları al
existing_files = set(os.listdir(image_dir))

# Gerçekten var olan fotoğraf dosyalarını filtrele
df_filtered = df[df["image_filename"].isin(existing_files)]

# image_filename sütununu istersen silebilirsin
df_filtered = df_filtered.drop(columns=["image_filename"])

# Temizlenmiş veriyi yeni bir CSV dosyasına kaydet
df_filtered.to_csv(output_path, index=False)

print(f"Temizlenmiş CSV başarıyla kaydedildi: {output_path}")
print(f"Orijinal satır sayısı: {len(df)}")
print(f"Temizlenmiş satır sayısı: {len(df_filtered)}")
import pandas as pd

df = pd.read_csv("updated_merged_dataset.csv")

# Aynı image_path'e sahip kaç tekrar var?
duplicate_counts = df["image_path"].value_counts()
duplicates = duplicate_counts[duplicate_counts > 1]

print(f"Tekrar eden image_path sayısı: {len(duplicates)}")
print("Örnek tekrar eden kayıtlar:")
print(duplicates.head(10))
