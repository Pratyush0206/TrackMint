# 📱 Track Mint

An Android application that automatically tracks your expenses by reading SMS notifications and presents them in a clean, searchable interface. The app helps users monitor monthly spending without manually entering transactions.

---

## ✨ Features

- 📩 Automatic SMS transaction detection
- 💾 Offline storage using Room Database
- 🔄 Sync new SMS transactions anytime
- 📅 Monthly transaction filtering
- 🔍 Search transactions by:
    - Name
    - Amount
    - Type
    - Date
- 💸 Monthly expenditure summary
    - Total Received
    - Total Spent
    - Net Expenditure
- 🚫 Include / Exclude transactions from calculations
- 📝 Add personal notes to every transaction
- 📌 Note indicator on transaction cards
- 📄 Detailed transaction view
- 🎨 Modern Material 3 UI

---
## 📸 Screenshots

### 🏠 Home Screen
Displays the monthly transaction history, expenditure summary, search functionality, and synchronization options.

<p align="center">
  <img src="Screenshots/Home.jpg" width="280"/>
</p>

---

### 📄 Transaction Details
View complete transaction information, add personal notes, and check whether the transaction is included in expense calculations.

<p align="center">
  <img src="Screenshots/Details.jpg" width="280"/>
</p>

---
## 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Room Database
- Material 3
- Coroutines
- Android SMS API

---

## 🏗 Architecture

```
SMS Inbox
      │
      ▼
 SMS Parser
      │
      ▼
 Room Database
      │
      ▼
 Repository
      │
      ▼
 Jetpack Compose UI
```

---

## 📂 Project Structure

```
app/
 ├── Database
 ├── Repository
 ├── SMS Reader
 ├── SMS Parser
 ├── UI
 ├── Transaction Details Dialog
 └── Main Activity
```

---

## 🚀 Installation

1. Clone the repository

```bash
git clone https://github.com/yourusername/ExpenseTracker.git
```

2. Open in Android Studio.

3. Build and run the project.

4. Grant **SMS Read** permission when prompted.

---

## 📖 How It Works

1. The app reads SMS messages after permission is granted.
2. Bank transaction messages are parsed automatically.
3. Parsed transactions are stored in the Room database.
4. Users can:
    - Search transactions
    - Filter by month
    - Exclude transactions
    - Add personal notes
5. The dashboard automatically updates expenditure statistics.

---

## 🔮 Future Improvements

- Expense categories
- Charts & analytics
- Export to CSV/PDF
- Budget tracking
- Cloud backup
- Recurring expense detection
- Dark/Light theme toggle

---

## 👨‍💻 Author

**Pratyush Prasad**


---

