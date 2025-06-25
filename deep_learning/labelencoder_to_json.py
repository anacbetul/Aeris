import pickle, json

with open("label_encoder.pkl", "rb") as f:
    encoder = pickle.load(f)

with open("labels.json", "w") as f:
    json.dump(encoder.classes_.tolist(), f)
