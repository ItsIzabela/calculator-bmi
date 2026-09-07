# DOKUMENTACJA APLIKACJI BMI

**Tytuł:** Dokumentacja aplikacji BMI  
**Imię i nazwisko zdającego:** Izabela Majcherczyk  
**Numer PESEL:** XYZ  
**Data wykonania:** 07.09.2026.  

---

## Spis treści

<!-- toc -->

- [DOKUMENTACJA APLIKACJI BMI](#dokumentacja-aplikacji-bmi)
  - [Spis treści](#spis-treści)
  - [1. Opis działania aplikacji konsolowej](#1-opis-działania-aplikacji-konsolowej)
    - [1.1 Pseudokod metody calculate\_bmi()](#11-pseudokod-metody-calculate_bmi)
    - [1.2 Opis walidacji danych](#12-opis-walidacji-danych)
  - [2. Opis aplikacji mobilnej oraz webowej](#2-opis-aplikacji-mobilnej-oraz-webowej)
    - [2.1 Opis działania walidacji w aplikacjach GUI](#21-opis-działania-walidacji-w-aplikacjach-gui)
  - [3. Dokumentacja testów](#3-dokumentacja-testów)
    - [3.1 Tabela z przypadkami testowymi](#31-tabela-z-przypadkami-testowymi)
    - [3.2 Opis procedury testowej](#32-opis-procedury-testowej)
    - [3.3 Wnioski z testów](#33-wnioski-z-testów)
  - [4. Instrukcja użytkownika](#4-instrukcja-użytkownika)
    - [4.1 Uruchomienie aplikacji konsolowej](#41-uruchomienie-aplikacji-konsolowej)
    - [4.2 Obsługa aplikacji mobilnej i webowej](#42-obsługa-aplikacji-mobilnej-i-webowej)

<!-- /toc -->

---

## 1. Opis działania aplikacji konsolowej

Aplikacja konsolowa w języku Python służy do szybkiego obliczania wskaźnika masy ciała (BMI) oraz jego interpretacji na podstawie danych podanych przez użytkownika (waga w kg, wzrost w m).

### 1.1 Pseudokod metody calculate_bmi()

```text
FUNKCJA calculate_bmi():
    WEJŚCIE: weight (waga w kg), height (wzrost w m)
    
    OBICZ bmi = weight / (height * height)
    ZAOKRĄGL bmi do 2 miejsc po przecinku
    
    ZWRÓĆ bmi
KONIEC FUNKCJI
```
### 1.2 Opis walidacji danych

Walidacja danych odbywa się za pomocą właściwości (*properties*) oraz setterów w klasie `BMICalculator`:

* **Waga (`weight`):**
  * Sprawdzany warunek: `2.0 <= weight <= 300.0`.
  * W przypadku podania wartości poniżej 2 kg lub powyżej 300 kg wyrzucany jest wyjątek `ValueError("Waga nie może być poniżej 2kg ani powyżej 300kg")`.
* **Wzrost (`height`):**
  * Sprawdzany warunek: `0.5 <= height <= 2.5`.
  * W przypadku podania wartości poniżej 0.5 m lub powyżej 2.5 m wyrzucany jest wyjątek `ValueError("Wzrost nie może być poniżej 0.5 m ani powyżej 2,5m")`.


## 2. Opis aplikacji mobilnej oraz webowej

Aplikacja mobilna (Android / Jetpack Compose) oraz wersja webowa (HTML5/CSS3/JavaScript) pozwalają na interaktywne wprowadzanie parametrów ciała i uzyskanie natychmiastowego wyniku z kolorystyczną interpretacją.


### 2.1 Opis działania walidacji w aplikacjach GUI

* **Aplikacja mobilna (Android):**
  * Pola tekstowe akceptują jedynie znaki numeryczne oraz kropkę/przecinek.
  * Automatyczna zamiana zamienia przecinki na kropki (`replace(',', '.')`).
  * Jeśli wprowadzone wartości wykraczają poza zakres lub są puste, pod polem tekstowym pojawia się czerwony komunikat błędu (*„Wprowadź poprawną wagę (1-300 kg)”*).
* **Aplikacja webowa (JS):**
  * Sprawdza, czy pola nie są puste (`!height || !weight`) oraz czy podane wartości są większe od zera.
  * W przypadku błędu wyświetlany jest alert informacyjny `alert("Wprowadź poprawne wartości!")`.


---

## 3. Dokumentacja testów

### 3.1 Tabela z przypadkami testowymi

| Nr testu | Dane wejściowe (wzrost, masa) | Oczekiwane BMI | Oczekiwana interpretacja | Wynik testu |
| :---: | :---: | :---: | :---: | :---: |
| **1** | 1,75 m, 70 kg | 22,86 | Waga prawidłowa | Pozytywny |
| **2** | 1,60 m, 45 kg | 17,58 | Niedowaga | Pozytywny |
| **3** | 1,80 m, 90 kg | 27,78 | Nadwaga | Pozytywny |
| **4** | 1,70 m, 95 kg | 32,87 | Otyłość | Pozytywny |
| **5** | 1,75 m, 1 kg | Błąd (`ValueError`) | Odrzucenie wartości (waga < 2 kg) | Pozytywny |
| **6** | 0,40 m, 70 kg | Błąd (`ValueError`) | Odrzucenie wartości (wzrost < 0,5 m) | Pozytywny |

### 3.2 Opis procedury testowej

1. **Testy jednostkowe (Automatyczne):** Wykonane za pomocą frameworka `pytest` w języku Python. Sprawdzają one metody klasy `BMICalculator` oraz zapis do pliku z użyciem mockowania danych wejściowych (`monkeypatch`).

### 3.3 Wnioski z testów

Wszystkie zaplanowane przypadki testowe zakończyły się sukcesem. Aplikacja poprawnie oblicza BMI, klasyfikuje wynik do odpowiedniej grupy oraz skutecznie blokuje nieprawidłowe dane wejściowe. Zapis do plików/localStorage działa prawidłowo.


---

---

## 4. Instrukcja użytkownika

### 4.1 Uruchomienie aplikacji konsolowej

1. Upewnij się, że masz zainstalowany Python 3.8 lub nowszy.
2. Otwórz terminal w katalogu głównym projektu.
3. Uruchom program poleceniem: python -m aplikacja_konsolowa.Program
4. Podaj wagę (w kg) oraz wzrost (w metrach, np. 1.75), po czym naciśnij Enter.
5. Wynik zostanie wyświetlony w konsoli i automatycznie zapisany w pliku wynik_BMI.txt w folderze aplikacja_konsolowa.

### 4.2 Obsługa aplikacji mobilnej i webowej

* Aplikacja Mobilna:
  1. Uruchom aplikację na smartfonie lub emulatorze Android Studio.
  2. Wpisz swoją wagę w pierwszym polu tekstowym oraz wzrost (w cm) w drugim.
  3. Kliknij przycisk "Oblicz BMI".
  4. Aby zapisać wynik w pamięci urządzenia, naciśnij przycisk "Zapisz wynik do pliku".

* Aplikacja Webowa:
  1. Otwórz plik index.html w dowolnej przeglądarce internetowej.
  2. Wprowadź wzrost (w cm) oraz wagę (w kg).
  3. Kliknij przycisk "Oblicz BMI".
  4. Wynik z wyróżnieniem kolorystycznym pojawi się na ekranie, a ostatnie 5 wyników zostanie automatycznie zapamiętanych w sekcji historii.