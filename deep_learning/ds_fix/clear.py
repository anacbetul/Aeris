# import pandas as pd
# import os

# # Mevcut dataset.csv dosyasını oku
# df = pd.read_csv("dataset.csv")

# # Mont klasöründeki tüm görselleri al
# mont_dir = "C:\\Users\\21360859036\\Desktop\\Mont"
# new_rows = []

# for filename in os.listdir(mont_dir):
#     if filename.lower().endswith((".jpg", ".jpeg", ".png", ".webp")):  # görsel uzantıları
#         img_id = os.path.splitext(filename)[0]
#         img_path = os.path.join(mont_dir, filename).replace("\\", "/")  # yol uyumu
#         new_rows.append({
#             "id": img_id,
#             "articleType": "Coat",
#             "image_path": img_path
#         })

# # Yeni verileri DataFrame olarak oluştur
# new_df = pd.DataFrame(new_rows)

# # Eski veriye yeni verileri ekle
# df_updated = pd.concat([df, new_df], ignore_index=True)

# # Yeni CSV dosyasını kaydet
# df_updated.to_csv("dataset_updated.csv", index=False)
# print("Yeni veriler eklendi: dataset_updated.csv")






# import pandas as pd
# import os

# # CSV dosyasını oku
# df = pd.read_csv("dataset.csv")

# # tracksuit olup id içinde pants geçen satırları filtrele
# mask = (df["articleType"] == "Tracksuit") & (df["id"].str.contains("pants", case=False, na=False))
# tracksuit_pants_rows = df[mask]

# # Silinecek görsellerin path'lerini al
# image_paths_to_delete = tracksuit_pants_rows["image_path"].tolist()

# # Görselleri klasörden sil (örnek klasör: merged_images)
# for img_path in image_paths_to_delete:
#     # Dosya adını al (Windows/Linux uyumu için)
#     filename = os.path.basename(img_path)
#     full_path = os.path.join("merged_images", filename)
    
#     if os.path.exists(full_path):
#         os.remove(full_path)
#         print(f"Silindi: {full_path}")
#     else:
#         print(f"Bulunamadı: {full_path}")

# # Filtrelenen satırları veriden çıkar
# df = df.drop(tracksuit_pants_rows.index)

# # Güncellenmiş CSV dosyasını kaydet
# df.to_csv("dataset_cleaned.csv", index=False)
# print("Güncellenmiş CSV kaydedildi: dataset_cleaned.csv")

# import pandas as pd
# import os

# # CSV dosyasını oku
# df = pd.read_csv("dataset.csv")

# # articleType'ı 'Coat' olanları filtrele
# coat_df = df[df["articleType"] == "Coat"]

# # Eğer 67'den az veri varsa uyarı ver
# if len(coat_df) < 67:
#     raise ValueError(f"Sadece {len(coat_df)} tane 'Coat' verisi var, 67 tanesi silinemez.")

# # Rastgele 67 tane seç
# coat_to_delete = coat_df.sample(n=67, random_state=42)

# # Silinecek görsellerin path'lerini al
# image_paths_to_delete = coat_to_delete["image_path"].tolist()

# # Görselleri klasörden sil
# for img_path in image_paths_to_delete:
#     filename = os.path.basename(img_path)
#     full_path = os.path.join("merged_images", filename)
#     if os.path.exists(full_path):
#         os.remove(full_path)
#         print(f"Silindi: {full_path}")
#     else:
#         print(f"Bulunamadı: {full_path}")

# # Veri çerçevesinden bu 67 satırı çıkar
# df = df.drop(coat_to_delete.index)

# # Güncellenmiş CSV dosyasını kaydet
# df.to_csv("dataset_cleaned.csv", index=False)
# print("Güncellenmiş CSV kaydedildi: dataset_cleaned.csv")
