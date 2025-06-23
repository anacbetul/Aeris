import pandas as pd
import numpy as np
import os
import cv2
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import classification_report, confusion_matrix
import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, GlobalAveragePooling2D
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.callbacks import EarlyStopping, ReduceLROnPlateau
from tensorflow.keras.applications import InceptionV3
from tensorflow.keras.applications.inception_v3 import preprocess_input
import pickle

# Callbacks
early_stop = EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)
reduce_lr = ReduceLROnPlateau(monitor='val_loss', patience=3, factor=0.5, verbose=1)

# Dataset
csv_path = "dataset.csv"
df = pd.read_csv(csv_path, on_bad_lines='skip', low_memory=False)

image_dir = "merged_images"
image_size = (139, 139)  # InceptionV3 varsayılanı

# Görsel yükleme
def load_and_preprocess_image(image_path):
    corrected_path = os.path.normpath(image_path)
    full_path = os.path.join(image_dir, os.path.basename(corrected_path))
    img = cv2.imread(full_path)
    if img is None:
        print(f"HATA: {full_path} yüklenemedi.")
        return None
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img = cv2.resize(img, image_size)
    img = preprocess_input(img)
    return img

# Veriyi yükle
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

# Numpy dizilerine çevir
X = np.array(images)
y = np.array(labels)

# Label encode
le = LabelEncoder()
y = le.fit_transform(y)

# Train-test split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# Veri artırma
datagen = ImageDataGenerator(
    rotation_range=30,
    width_shift_range=0.15,
    height_shift_range=0.15,
    shear_range=0.15,
    zoom_range=0.2,
    horizontal_flip=True,
    fill_mode='nearest'
)
datagen.fit(X_train)

# GoogLeNet = InceptionV3
base_model = InceptionV3(weights='imagenet', include_top=False, input_shape=(139, 139, 3))
base_model.trainable = True
for layer in base_model.layers[:-50]:  # son 50 katman eğitilebilir
    layer.trainable = False

model = Sequential([
    base_model,
    GlobalAveragePooling2D(),
    Dense(128, activation='relu'),
    Dropout(0.5),
    Dense(len(np.unique(y)), activation='softmax')
])

# Model derleme
optimizer = tf.keras.optimizers.Adam(learning_rate=1e-4)
model.compile(optimizer=optimizer, loss='sparse_categorical_crossentropy', metrics=['accuracy'])

model.summary()

# Model eğitimi
history = model.fit(
    datagen.flow(X_train, y_train, batch_size=32),
    validation_data=(X_test, y_test),
    epochs=15,
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
y_pred_probs = model.predict(X_test)
y_pred = np.argmax(y_pred_probs, axis=1)

print(" Classification Report:")
print(classification_report(y_test, y_pred, target_names=le.classes_))

print(" Confusion Matrix:")
print(confusion_matrix(y_test, y_pred))

# Model kaydet
model.save("googlenet_inceptionv3_modeli.h5")

# LabelEncoder kaydet
with open("label_encoder_googlenet_inceptionv3.pkl", "wb") as f:
    pickle.dump(le, f)

print("Model ve LabelEncoder başarıyla kaydedildi.")
