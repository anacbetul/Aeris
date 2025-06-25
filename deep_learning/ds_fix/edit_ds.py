import os
import pandas as pd

# Dataset klasörünün yolu
dataset_path = "deneme3\\Clothes_Dataset"

# Klasör isimlerini Türkçe etiketlere çeviren sözlük
label_mapping = {
    "Blazer": "Jacket",
    "Celana_Panjang": "Tracksuit",
    "Celana_Pendek": "Short",
    "Gaun": "Dress",
    "Hoodie": "Hoodie",
    "Jaket": "Raincoat",
    "Jaket_Denim": "Denimcoat",
    "Jaket_Olahraga": "SportCoat",
    "Jeans": "Pant",
    "Kaos": "T-shirt",
    "Kemeja": "Shirt",
    "Mantel": "Coat",
    "Polo": "T-shirt",
    "Rok": "Skirt",
    "Sweter": "Sweter"
}

# Etiketleri tutacak liste
data = []
# Klasörleri dolaş
for folder_name in os.listdir(dataset_path):
    folder_path = os.path.join(dataset_path, folder_name)
    
    if os.path.isdir(folder_path) and folder_name in label_mapping:
        turkce_label = label_mapping[folder_name]
        for filename in os.listdir(folder_path):
            if filename.lower().endswith(('.png', '.jpg', '.jpeg')):
                image_path = os.path.join(folder_name, filename)
                image_id = os.path.splitext(filename)[0]  # Dosya adından uzantıyı çıkar (örn. "1234.jpg" → "1234")
                data.append({'id': image_id, 'articleType': turkce_label})

# DataFrame oluştur ve CSV'ye kaydet
df = pd.DataFrame(data)
df.to_csv('clothes_labels_tr.csv', index=False)

print("Dosya adlarından alınan ID'lerle CSV başarıyla oluşturuldu.")