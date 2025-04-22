# 📊 Applicazione Java - Gestione Spese Familiari

Questa è un'applicazione **desktop** sviluppata in Java per la **gestione delle spese familiari**, pensata per fornire una soluzione semplice e intuitiva per il monitoraggio del bilancio personale o familiare.

## ✨ Funzionalità principali

- ✅ Registrazione e login multiutente
- ➕ Inserimento di **entrate** e **uscite**
- 🔄 Modifica e cancellazione dei movimenti
- 📈 Dashboard con **grafico mensile dinamico**
- 💰 Calcolo e visualizzazione del saldo aggiornato
- 🔍 Filtro movimenti per data 
- 👁️ Interfaccia coerente e moderna grazie all'uso avanzato di `Swing` e `GridBagLayout`

## 🛠️ Tecnologie utilizzate

### Backend
- **Java SE 17**
- **JPA / Hibernate** – Gestione della persistenza dati
- **MySQL** – Database relazionale
- **Architettura a strati** – Separazione in:
  - Controller (logica applicativa)
  - Service (interfacce + implementazioni)
  - Entity (classi mappate per JPA)
  - View (interfaccia grafica in Swing)
- **Pattern MVC + uso di interfacce** – Per garantire modularità e facilità di test e manutenzione

### Frontend
- **Java Swing** – Interfaccia grafica desktop moderna, responsive e strutturata
- **Grafici dinamici** – Generati con `JFreeChart` o libreria personalizzata per rappresentare visivamente entrate, uscite e saldo mensile

## 📊 Grafico Spese

Il grafico integrato nella dashboard rappresenta:
- Entrate (barra verde)
- Uscite (barra rossa)
- Saldo (valore evidenziato in giallo)

I dati sono aggregati **mese per mese** e si aggiornano in tempo reale quando l’utente aggiunge, modifica o rimuove un movimento.

## 📁 Struttura del progetto
src/ ├── Controller/ ├── Service/ │ ├── interfaces/ │ └── impl/ ├── Entity/ ├── View/ └── utils/