from tensorflow.keras.models import load_model
import pickle
import numpy as np
import cv2
import matplotlib.pyplot as plt
from tensorflow.keras.applications.mobilenet_v2 import preprocess_input

#  1. Tahmin fonksiyonu tanımı
def predict_and_display(image_path, model, label_encoder, image_size=(128, 128)):
    # Görseli yükle
    img = cv2.imread(image_path)
    if img is None:
        print(f"HATA: {image_path} yüklenemedi.")
        return
    original_img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    # İşlemden geçir
    img = cv2.resize(original_img, image_size)
    img = preprocess_input(img)
    img = np.expand_dims(img, axis=0)

    # Tahmin yap
    prediction = model.predict(img)
    predicted_class_index = np.argmax(prediction)
    predicted_label = label_encoder.inverse_transform([predicted_class_index])[0]

    # Sonucu yazdır ve görseli göster
    print(f" Tahmin edilen sınıf: {predicted_label}")

    plt.figure(figsize=(4, 4))
    plt.imshow(original_img)
    plt.title(f"Tahmin: {predicted_label}", fontsize=14)
    plt.axis('off')
    plt.show()

    return predicted_label

#  2. Model ve LabelEncoder yükle
model = load_model("mobilenet_giyim_modeli_datagen_dropout.h5")

with open("label_encoder.pkl", "rb") as f:
    le = pickle.load(f)

#  3. Test görselini tahmin et
# test_image_path = "C:\\Users\\21360859036\\Desktop\\ootd\\deepfashion\\images\\2685.jpg"
test_image_path= "C:\\Users\\21360859036\\Desktop\\5.jpg"
tahmin = predict_and_display(test_image_path, model, le)
