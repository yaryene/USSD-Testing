# 📱 USSD Integration Guide (Africa’s Talking + ngrok)

This guide explains how to set up and test a **USSD application** using:

* Africa’s Talking (USSD sandbox)
* ngrok (to expose your local backend)
* Your own backend (e.g., Spring Boot)

---

# 🧠 Overview

USSD works by sending HTTP requests from a telecom gateway to your backend.

Flow:

```
User → USSD Code → Africa’s Talking → Your Backend → Response → User
```

---

# 🚀 Step 1: Register on Africa’s Talking

1. Go to the Africa’s Talking website
2. Create an account
3. Switch to **Sandbox Mode**
4. Navigate to:

```
USSD → Service Codes
```

---

# 🌐 Step 2: Install and Setup ngrok

## Install ngrok (Mac)

```bash
brew install ngrok
```

---

## Authenticate ngrok

Visit:

```
https://dashboard.ngrok.com/get-started/setup/macos
```

Copy your auth token and run:

```bash
ngrok config add-authtoken YOUR_TOKEN_HERE
```

---

## Start ngrok

```bash
ngrok http 8080
```

You will see something like:

```
Forwarding https://abc123.ngrok-free.dev -> http://localhost:8080
```

👉 This is your **public URL** (used as callback)

---

# ⚙️ Step 3: Create USSD Channel

In Africa’s Talking dashboard:

```
USSD → Create Channel
```

### Fill in the details:

* **Channel Name**

  ```
  My USSD App
  ```

* **Service Code**

  ```
  *384*31323#
  ```

  ⚠️ If unavailable, use suggested alternatives (e.g., `*384*43017#`)

* **Callback URL**

  ```
  https://abc123.ngrok-free.dev/ussd
  ```

👉 Replace with your actual ngrok forwarding URL

Click **Create Channel**

---

# ▶️ Step 4: Run Your Backend

Start your backend server locally (e.g., on port 8080):

```
http://localhost:8080
```

Ensure your USSD endpoint is reachable at:

```
/ussd
```

---

# 🧪 Step 5: Test Using Simulator

1. Go to Africa’s Talking dashboard
2. Click **Launch Simulator**

---

## In the simulator:

### 1. Enter phone number:

```
251912345678
```

Click **Connect**

---

### 2. Dial your USSD code:

```
*384*31323#
```

---

## Expected Interaction Example

### Step 1:

```
Welcome to Mobile Banking.
Please enter your PIN:
```

---

### Step 2 (User enters PIN):

```
1234
```

---

### Step 3:

```
1. My Account
2. Own Account Transfer
```

---

### Step 4 (User selects option):

```
1
```

---

### Final Response:

```
Your balance is 5000 ETB
```

---

# 🔍 Key Concepts

## USSD Response Types

* `CON` → Continue session
* `END` → End session

---

## `text` Parameter Behavior

| Step | User Input | text value |
| ---- | ---------- | ---------- |
| 1    | Dial       | ""         |
| 2    | PIN        | "1234"     |
| 3    | Next       | "1234*1"   |

---

# ⚠️ Common Issues

### ❌ No response in simulator

* ngrok not running
* Incorrect callback URL

---

### ❌ Timeout

* Backend server not running

---

### ❌ 404 Error

* Incorrect endpoint path (should be `/ussd`)

---

# 🧱 Next Improvements

* Integrate real authentication APIs
* Add session management (e.g., Redis or in-memory store)
* Implement multi-step flows (transfers, payments)
* Add validation and error handling

---

# 🎯 Summary

You now have:

✅ Live USSD integration
✅ Public callback using ngrok
✅ Working backend connection
✅ Interactive simulator testing

---

You can now extend this into a **production-grade USSD system** 🚀
