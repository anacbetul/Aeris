import pandas as pd

# CSV dosyasını oku
csv_path = "dataset.csv"
df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)

# articleType'a göre say
counts = df["articleType"].value_counts()

# Sonuçları yazdır
print("Sinif daigilimi:")
print(counts)
