import os
import pytest
from aplikacja_konsolowa.BMICalculator import BMICalculator
from aplikacja_konsolowa.Program import Program


# 1. Poprawność obliczania BMI dla różnych danych wejściowych
def test_calculate_bmi():
    # Metoda calculate_bmi() zwraca wynik zaokrąglony do 2 miejsc po przecinku
    assert BMICalculator(70, 1.75).calculate_bmi() == 22.86
    assert BMICalculator(50, 1.60).calculate_bmi() == 19.53
    assert BMICalculator(90, 1.80).calculate_bmi() == 27.78


# 2. Poprawność interpretacji wyniku
def test_interpret_bmi():
    calculator = BMICalculator(70, 1.75)
    
    # Niedowaga (< 18.5)
    assert calculator.interpret_bmi(17.0) == "niedowaga"
    
    # Waga prawidłowa (18.5 - 24.9)
    assert calculator.interpret_bmi(22.86) == "waga prawidlowa"
    
    # Nadwaga (25.0 - 29.9)
    assert calculator.interpret_bmi(27.0) == "nadwaga"
    
    # Otyłość (>= 30.0)
    assert calculator.interpret_bmi(32.0) == "otylosc"


# 3. Walidacja danych (odrzucanie nieprawidłowych wartości)
def test_weight_validation():
    with pytest.raises(ValueError):
        BMICalculator(1, 1.75)  # Waga poniżej 2kg
    with pytest.raises(ValueError):
        BMICalculator(301, 1.75)  # Waga powyżej 300kg


def test_height_validation():
    with pytest.raises(ValueError):
        BMICalculator(70, 0.4)  # Wzrost poniżej 0.5m
    with pytest.raises(ValueError):
        BMICalculator(70, 2.6)  # Wzrost powyżej 2.5m


# 4. Zapis do pliku 
def test_save_to_file(monkeypatch, tmp_path):
    # Symulacja wpisywania danych przez użytkownika w konsoli (70 kg, 1.75 m)
    inputs = iter(["70", "1.75"])
    monkeypatch.setattr('builtins.input', lambda _: next(inputs))

    monkeypatch.chdir(tmp_path)
    os.makedirs("aplikacja_konsolowa", exist_ok=True)

    # Uruchomienie programu
    program = Program()
    program.run()

    file_path = tmp_path / "aplikacja_konsolowa" / "wynik_BMI.txt"

    # Sprawdzenie czy plik istnieje
    assert file_path.exists()

    # Odczyt i weryfikacja zawartości pliku
    content = file_path.read_text()
    assert "Waga: 70.0 kg" in content
    assert "Wzrost: 1.75 m" in content
    assert "Wynik BMI: 22.86" in content
    assert "Interpretacja BMI: waga prawidlowa" in content