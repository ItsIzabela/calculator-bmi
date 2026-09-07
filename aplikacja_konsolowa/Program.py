from BMICalculator import BMICalculator

class Program:
    def __init__(self): # pobranie danych od użytkownika i inicjalizacja klasy BMICalculator
        print("Witaj w kalkulatorze BMI!")
        self.user_weight = float(input("Podaj swoją wagę (kg): "))
        self.user_height = float(input("Podaj swój wzrost (m): "))
        self.calculate = BMICalculator(self.user_weight, self.user_height)

    def saveToFile(self): # funkcja zapisująca wagę, wzrost, wynik BMI i interpretację do pliku tekstowego
        with open("aplikacja_konsolowa/wynik_BMI.txt", "w") as file:
            bmi_value = self.calculate.calculate_bmi()
            interpretation = self.calculate.interpret_bmi(bmi_value)
            file.write("Waga: " + str(self.user_weight) + " kg\n")
            file.write("Wzrost: " + str(self.user_height) + " m\n")
            file.write("Wynik BMI: " + str(bmi_value) + "\n")
            file.write("Interpretacja BMI: " + interpretation + "\n")

    def run(self): # funkcja uruchamiająca program
        bmi_value = self.calculate.calculate_bmi()
        interpretation = self.calculate.interpret_bmi(bmi_value)
        print(f"Waga: {self.user_weight} kg")
        print(f"Wzrost: {self.user_height} m")
        print(f"Wynik BMI: {bmi_value}")
        print(f"Interpretacja BMI: {interpretation}")
        self.saveToFile()
        print("Wynik zapisany do pliku wynik_BMI.txt")

if __name__ == "__main__": # uruchomienie programu
    program = Program()
    program.run()
