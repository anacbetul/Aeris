import pandas as pd

# CSV dosyasını oku
df = pd.read_csv("dataset.csv", on_bad_lines='skip', low_memory=False)

# Flats → Heels
df['articleType'] = df['articleType'].replace('Trousers', 'Pant')

# Değişiklikleri aynı dosyaya kaydet
df.to_csv("dataset.csv", index=False)

print("Flats etiketleri başarıyla Heels olarak değiştirildi.")
