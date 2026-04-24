# Expense Tracker (Java CLI)

A simple command-line Expense Tracker built in Java.  
It allows users to create, view, update, delete, and analyze expenses stored in a JSON file.

---

## Features

• Add new expenses  
• View all expenses  
• Update expenses by ID  
• Delete expenses by ID  
• View total expenses (summary)  
• Filter expenses by minimum price  
• Auto-generated IDs for each expense  
• Persistent storage using `expenses.json`

---

## Usage

To Add an expense:
  java ExpenseApp <description> <amount>

To view the history:
  java ExpenseApp view

To Update an expense:
  java ExpenseApp update <id> <description> <amount>

To delete an expense:
  java ExpenseApp delete <id>

To view the total expenses so far:
  java ExpenseApp summary

