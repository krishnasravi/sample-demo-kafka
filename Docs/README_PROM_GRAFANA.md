# 📊 Prometheus & Grafana Setup in Minikube (Background Access)

This guide shows how to deploy **Prometheus and Grafana in Minikube**, and **expose Grafana in the background** using Helm and `kubectl port-forward`.

---

## ✅ Step 1: Start Minikube

```bash
minikube start
```

---

## ✅ Step 2: Create the `monitoring` Namespace

```bash
kubectl create namespace monitoring
```

---

## ✅ Step 3: Add Helm Repo and Install Prometheus + Grafana

### Add Helm Repo

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
```

### Install kube-prometheus-stack (includes Prometheus + Grafana)

```bash
helm install monitoring-stack prometheus-community/kube-prometheus-stack --namespace monitoring
```

---

## ✅ Step 4: Expose Grafana in the Background

Grafana is deployed as a service in the `monitoring` namespace. We will expose it using `kubectl port-forward` or `minikube service`.

### Option A: Port-forward Grafana in background

```bash
nohup kubectl port-forward -n monitoring svc/monitoring-stack-grafana 3000:80 > port-forward.log 2>&1 &
```

This will run the Grafana service in the background and allow access at:

👉 http://localhost:3000

### Option B: Open Grafana via Minikube

```bash
minikube service -n monitoring monitoring-stack-grafana &
```

This will open the service in your default browser.

---

## ✅ Step 5: Log in to Grafana

### Default Credentials

- **Username**: `admin`
- **Password**: `prom-operator`

To get the exact password:

```bash
kubectl get secret --namespace monitoring monitoring-stack-grafana -o jsonpath="{.data.admin-password}" | base64 --decode
```

---

You can now access Grafana on [http://localhost:3000](http://localhost:3000) and start visualizing Kubernetes metrics.
