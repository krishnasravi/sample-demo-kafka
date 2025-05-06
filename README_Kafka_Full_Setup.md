
# ☕ Kafka-based Spring Boot Application on Kubernetes

This guide outlines how to build, deploy, and test a Kafka-based Spring Boot application using Minikube and Kubernetes.

---

## 📦 1. Spring Boot Kafka Application

The Spring Boot app includes:

- REST Controller
- Kafka Producer
- Kafka Consumer
- `application.properties`

---

## 📄 2. Kubernetes Manifests

### a. Kafka + Zookeeper Setup

**File: `kafka-zookeeper.yaml`**
```yaml
---
apiVersion: v1
kind: Service
metadata:
  name: zookeeper
spec:
  ports:
    - port: 2181
  selector:
    app: zookeeper
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: zookeeper
spec:
  selector:
    matchLabels:
      app: zookeeper
  replicas: 1
  template:
    metadata:
      labels:
        app: zookeeper
    spec:
      containers:
        - name: zookeeper
          image: bitnami/zookeeper:latest
          ports:
            - containerPort: 2181
---
apiVersion: v1
kind: Service
metadata:
  name: kafka
spec:
  ports:
    - port: 9092
  selector:
    app: kafka
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kafka
spec:
  selector:
    matchLabels:
      app: kafka
  replicas: 1
  template:
    metadata:
      labels:
        app: kafka
    spec:
      containers:
        - name: kafka
          image: bitnami/kafka:latest
          ports:
            - containerPort: 9092
          env:
            - name: KAFKA_CFG_ZOOKEEPER_CONNECT
              value: zookeeper:2181
            - name: KAFKA_CFG_ADVERTISED_LISTENERS
              value: PLAINTEXT://kafka:9092
            - name: ALLOW_PLAINTEXT_LISTENER
              value: "yes"
```

---

### b. Spring Boot App Deployment

**File: `kafka-app.yaml`**
```yaml
apiVersion: v1
kind: Service
metadata:
  name: kafka-producer
spec:
  type: NodePort
  selector:
    app: kafka-producer
  ports:
    - port: 8080
      targetPort: 8080
      nodePort: 30088
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kafka-producer
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kafka-producer
  template:
    metadata:
      labels:
        app: kafka-producer
    spec:
      containers:
        - name: kafka-producer
          image: your-dockerhub-id/kafka-producer:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_KAFKA_BOOTSTRAP_SERVERS
              value: kafka:9092
```

> Replace `your-dockerhub-id/kafka-producer:latest` with your actual Docker image name.

---

## 🐳 3. Dockerfile

**File: `Dockerfile`**
```dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/kafka-springboot-app-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🔧 4. Build & Push Docker Image

```bash
./mvnw clean package
docker build -t your-dockerhub-id/kafka-producer:latest .
docker push your-dockerhub-id/kafka-producer:latest
```

---

## 🚀 5. Deploy to Minikube

```bash
kubectl apply -f kafka-zookeeper.yaml
kubectl apply -f kafka-app.yaml
```

---

## ✅ 6. Test the Setup

### Option A: Using NodePort
```bash
curl -X POST "http://<minikube-ip>:30088/api/kafka/publish?message=HelloKafka"
```

### Option B: Using Port Forward
```bash
kubectl port-forward svc/kafka-producer 8080:8080
curl -X POST "http://localhost:8080/api/kafka/publish?message=HelloKafka"
```

---

If successful, the message will be logged by the Kafka consumer in the pod:
```bash
kubectl logs deploy/kafka-producer
# Output: Received message: HelloKafka
```
