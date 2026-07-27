# 📱 Track Mint

An Android application that automatically tracks expenses by reading bank SMS notifications and storing them locally. The app eliminates manual expense entry and provides an intuitive interface for searching, organizing, and managing transactions.

---

# ✨ Features

- 📩 Automatic SMS transaction detection
- 💾 Offline storage using Room Database
- 🔄 One-click SMS synchronization
- 📅 Monthly transaction filtering
- 🔍 Search transactions by
    - Name
    - Amount
    - Date
    - Type
- 💸 Monthly expenditure summary
    - Total Received
    - Total Spent
    - Net Expenditure
- 🚫 Include / Exclude transactions from calculations
- 📝 Add personal notes to every transaction
- 📌 Notes indicator on transactions
- 📄 Detailed transaction view
- 🎨 Material 3 UI built with Jetpack Compose

---

# 📸 Screenshots

### 🏠 Home Screen
Displays monthly transactions, expenditure summary, search functionality, and synchronization options.

<p align="center">
  <img src="Screenshots/Home.jpg" width="320"/>
</p>

---

### 📄 Transaction Details
View complete transaction information, add personal notes, and manage transaction details.

<p align="center">
  <img src="Screenshots/Details.jpg" width="320"/>
</p>

---

# 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Room Database
- Material 3
- Kotlin Coroutines
- Android SMS API

---

# 🏗 Architecture

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

# 📂 Project Structure

```
app/
│
├── DatabaseProvider.kt
├── ExpenseDatabase.kt
├── Transaction.kt
├── TransactionDao.kt
├── TransactionRepository.kt
├── TransactionSyncManager.kt
│
├── SmsParser.kt
├── SmsReader.kt
├── SmsReceiver.kt
│
├── MainActivity.kt
├── TransactionDetailsDialog.kt
│
└── ui.theme/
```

---

# 🚀 Installation

1. Clone the repository

```bash
git clone https://github.com/Pratyush0206/ExpenseTracker.git
```

2. Open the project in Android Studio.

3. Build and run the application.

4. Grant SMS permission when prompted.

5. Tap **Sync** to import transactions from SMS.

---

# 📖 How It Works

1. Reads bank SMS messages after permission is granted.
2. Parses debit and credit transactions automatically.
3. Stores transactions locally using Room Database.
4. Allows users to:
    - Search transactions
    - Filter by month
    - Exclude transactions
    - Add personal notes
5. Updates expenditure statistics instantly.

---

# 🔮 Future Improvements

- Expense categories
- Charts & analytics
- Budget planner
- Export to CSV/PDF
- Cloud backup
- Recurring transaction detection
- Home screen widget

---

# 👨‍💻 Author

**Pratyush Prasad**

---
