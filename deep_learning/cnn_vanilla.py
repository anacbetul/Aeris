import pandas as pd
import numpy as np
import os
import cv2
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.utils import class_weight
import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout, BatchNormalization
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.callbacks import EarlyStopping, ReduceLROnPlateau

# Erken durdurma ve öğrenme oranı azaltma
early_stop = EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)
reduce_lr = ReduceLROnPlateau(monitor='val_loss', patience=2, factor=0.5, verbose=1)

# CSV ve görsel yolu
csv_path = "merged_dataset.csv"
image_dir = "merged_images"
image_size = (128, 128)

# CSV'yi oku
df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)

# Görsel yükleme ve ön işleme fonksiyonu
def load_and_preprocess_image(image_path):
    corrected_path = os.path.normpath(image_path)
    full_path = os.path.join(image_dir, os.path.basename(corrected_path))
    img = cv2.imread(full_path)
    if img is None:
        print(f"HATA: {full_path} yüklenemedi.")
        return None
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img = cv2.resize(img, image_size)
    img = img / 255.0  # normalize
    return img

# Görselleri yükle
images, labels = [], []
fail_count = 0

for image_path, label in zip(df["image_path"], df["articleType"]):
    img = load_and_preprocess_image(image_path)
    if img is not None:
        images.append(img)
        labels.append(label)
    else:
        fail_count += 1

print(f"Yüklenen görsel sayısı: {len(images)}")
print(f"Yüklenemeyen görsel sayısı: {fail_count}")

# NumPy dizisine çevir
X = np.array(images)
y = np.array(labels)

# Etiket encode
le = LabelEncoder()
y = le.fit_transform(y)

# Train-Test ayır
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# Veri artırma (daha dengeli)
datagen = ImageDataGenerator(
    rotation_range=15,
    width_shift_range=0.1,
    height_shift_range=0.1,
    zoom_range=0.1,
    horizontal_flip=True
)
datagen.fit(X_train)

# Class weights hesapla (sınıf dengesizliği varsa önemli)
class_weights = class_weight.compute_class_weight('balanced', classes=np.unique(y_train), y=y_train)
class_weights = dict(enumerate(class_weights))

# CNN + BatchNorm modeli (sadeleştirilmiş)
cnn_model = Sequential([
    Conv2D(32, (3, 3), activation='relu', input_shape=(128, 128, 3)),
    BatchNormalization(),
    MaxPooling2D(pool_size=(2, 2)),

    Conv2D(64, (3, 3), activation='relu'),
    BatchNormalization(),
    MaxPooling2D(pool_size=(2, 2)),

    Conv2D(128, (3, 3), activation='relu'),
    BatchNormalization(),
    MaxPooling2D(pool_size=(2, 2)),

    Flatten(),
    Dense(256, activation='relu'),
    Dropout(0.5),
    Dense(len(np.unique(y)), activation='softmax')  # 23 sınıf için
])

# Derleme
cnn_model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=1e-4),
    loss='sparse_categorical_crossentropy',
    metrics=['accuracy']
)

cnn_model.summary()

# Eğitim
history = cnn_model.fit(
    datagen.flow(X_train, y_train, batch_size=32),
    validation_data=(X_test, y_test),
    epochs=25,
    class_weight=class_weights,
    callbacks=[early_stop, reduce_lr]
)

# Doğruluk grafiği
plt.plot(history.history['accuracy'], label='Eğitim Doğruluğu')
plt.plot(history.history['val_accuracy'], label='Doğrulama Doğruluğu')
plt.title("Eğitim vs Doğrulama Doğruluğu")
plt.xlabel("Epoch")
plt.ylabel("Doğruluk")
plt.legend()
plt.grid(True)
plt.show()

# Test sonuçları
y_pred_probs = cnn_model.predict(X_test)
y_pred = np.argmax(y_pred_probs, axis=1)

print("Classification Report:")
print(classification_report(y_test, y_pred, target_names=le.classes_))

print("Confusion Matrix:")
print(confusion_matrix(y_test, y_pred))
