import pandas as pd

# CSV dosyasını oku
csv_path = "dataset.csv"
df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)

# Benzersiz sınıfları al ve sırala
unique_classes = sorted(df["articleType"].dropna().unique())

# TXT dosyasına yaz
with open("labels.txt", "w", encoding="utf-8") as f:
    for label in unique_classes:
        f.write(f"{label}\n")

print("labels.txt başarıyla oluşturuldu.")