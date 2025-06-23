import pandas as pd
import matplotlib.pyplot as plt
import os

# Excel dosyasının yolu
excel_path = "C:\\Users\\21360859036\\Desktop\\Yeni klasör\\model analiz.xlsx"

# Grafiklerin ana klasörü
output_dir = "grafikler"
os.makedirs(output_dir, exist_ok=True)

# Sayfa (model) isimleri
sheet_names = ["MobileNet", "ResNet", "VGG16", "VGG19", "GoogleNet", "EfficientNetB0"]

# Sayfaları sırayla işle
for sheet in sheet_names:
    try:
        df = pd.read_excel(excel_path, sheet_name=sheet, header=2)

        # Sütunları normalize et
        df.columns = (
            df.columns
            .str.strip()
            .str.lower()
            .str.replace(r"\s+", "_", regex=True)
        )

        # Model klasörü oluştur
        model_dir = os.path.join(output_dir, sheet)
        os.makedirs(model_dir, exist_ok=True)

        ### 1. Accuracy Grafiği
        if "accuracy" in df.columns:
            plt.figure(figsize=(10, 6))
            plt.plot(df["accuracy"], label="accuracy", marker='o')

            if "val_accuracy" in df.columns:
                plt.plot(df["val_accuracy"], label="val_accuracy", marker='o')

            plt.title(f"{sheet} - Accuracy")
            plt.xlabel("Hiperparametre Konfigürasyonları")
            plt.ylabel("Accuracy")
            plt.ylim(0, 1)
            plt.legend()
            plt.grid(True)
            plt.tight_layout()
            plt.savefig(os.path.join(model_dir, "accuracy.png"))
            plt.close()

        ### 2. Loss Grafiği
        if "loss" in df.columns:
            plt.figure(figsize=(10, 6))
            plt.plot(df["loss"], label="loss", marker='o')

            if "val_loss" in df.columns:
                plt.plot(df["val_loss"], label="val_loss", marker='o')

            plt.title(f"{sheet} - Loss")
            plt.xlabel("Hiperparametre Konfigürasyonları")
            plt.ylabel("Loss")
            plt.ylim(0, 1)
            plt.legend()
            plt.grid(True)
            plt.tight_layout()
            plt.savefig(os.path.join(model_dir, "loss.png"))
            plt.close()

        ### 3. Precision / Recall / F1 Grafiği
        metrics = ["precision", "recall", "f1"]
        available_metrics = [m for m in metrics if m in df.columns]

        if available_metrics:
            plt.figure(figsize=(10, 6))
            for m in available_metrics:
                plt.plot(df[m], label=m, marker='o')

            plt.title(f"{sheet} - Precision / Recall / F1")
            plt.xlabel("Hiperparametre Konfigürasyonları")
            plt.ylabel("Metric Value")
            plt.ylim(0, 1)
            plt.legend()
            plt.grid(True)
            plt.tight_layout()
            plt.savefig(os.path.join(model_dir, "metrics.png"))
            plt.close()

    except Exception as e:
        print(f"Hata oluştu: {sheet} sayfası işlenemedi. Hata: {e}")

print(" Her mimari için 3 grafik başarıyla oluşturuldu.")
