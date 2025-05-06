# 🚀 DevOps Setup Guide: Argo CD on Minikube & Jenkins on Windows

This guide walks through:
- Installing Argo CD in Minikube
- Port-forwarding Argo CD to `localhost:8086`
- Installing Jenkins on Windows
- Accessing both UIs

---

## ✅ Prerequisites

- Minikube running locally  
- `kubectl` installed  
- Java installed on Windows (for Jenkins)  
- Internet access (for downloading components)

---

## 1️⃣ Argo CD Setup on Minikube

### Create Namespace

```bash
kubectl create namespace argocd
```

### Install Argo CD Components

```bash
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

---

## 2️⃣ Port-forward Argo CD to Port 8086

Run this in a separate terminal:

```bash
kubectl port-forward svc/argocd-server -n argocd 8086:443
```

### Access the Argo CD UI

👉 Open browser and go to:  
`https://localhost:8086`

---

## 3️⃣ Retrieve Argo CD Login Credentials

```bash
kubectl get secret argocd-initial-admin-secret -n argocd -o jsonpath="{.data.password}" | base64 -d
```

- **Username**: `admin`  
- **Password**: Output from the command above

---

## 4️⃣ Install Jenkins on Windows

### Download Jenkins

Go to:  
👉 [https://www.jenkins.io/download/](https://www.jenkins.io/download/)  
Download the `.war` file (not MSI).

### Run Jenkins

Open `Command Prompt` or `PowerShell` and run:

```bash
java -jar jenkins.war --httpPort=8080
```

Jenkins will start and be available at:

👉 `http://localhost:8080`

### Get Initial Admin Password

Find it in:

```
C:\Users\<YourUsername>\.jenkins\secrets\initialAdminPassword
```

Paste it into the Jenkins setup screen.

Then install **suggested plugins** and create your admin user.

---

## 🌐 Access Summary

| Service   | URL                        | Login Info               |
|-----------|----------------------------|---------------------------|
| Argo CD   | https://localhost:8086     | `admin / <retrieved>`     |
| Jenkins   | http://localhost:8080      | `admin / <set during UI>` |

---

## 🔄 Optional: Argo CD as NodePort (Not Required if Port-Forwarding Works)

If you prefer NodePort instead of port-forwarding:

```bash
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "NodePort"}}'
kubectl get svc argocd-server -n argocd
minikube ip
```

Then access:  
👉 `https://<minikube-ip>:<node-port>`

---

## ✅ What's Next?

- Add GitOps deployment through Argo CD  
- Connect Jenkins to GitHub + Kubernetes  
- Monitor using Prometheus + Grafana  
- Deploy microservices using CI/CD pipelines  
