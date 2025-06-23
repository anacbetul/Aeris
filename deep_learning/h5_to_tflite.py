import tensorflow as tf

# Keras modelini .h5 dosyasından yükleme
model = tf.keras.models.load_model("mobilenet_giyim_modeli_datagen_dropout.h5")

# TFLite dönüştürücüyü oluşturma
converter = tf.lite.TFLiteConverter.from_keras_model(model)

# Modeli TFLite formatına çevirme
tflite_model = converter.convert()

# TFLite modelini dosyaya kaydetme
with open("mobilenet_giyim_modeli_datagen_dropout.tflite", "wb") as f:
    f.write(tflite_model)