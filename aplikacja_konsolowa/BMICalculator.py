class BMICalculator:
    def __init__(self, weight: float, height: float): # inicjalizacja klasy BMICalculator z wagą i wzrostem użytkownika
        self.weight = weight
        self.height = height

    @property # getter dla wagi
    def weight(self):
        return self._weight

    @weight.setter # setter dla wagi + walidacja
    def weight(self, value):
        if value < 2 or value > 300:
            raise ValueError("Waga nie może być poniżej 2kg ani powyżej 300kg")
        print("Wpisywanie wagi")
        self._weight = value

    @property # getter dla wzrostu
    def height(self):
        return self._height

    @height.setter # setter dla wzrosru + walidacjas
    def height(self, value):
        if value < 0.5 or value > 2.5:
            raise ValueError("Wzrost nie może być poniżej 0.5 m ani powyżej 2,5m")
        print("Wpisywanie wzrostu")
        self._height = value

    def calculate_bmi(self) -> float: # fukcja do obliczenia BMI
        return round(self.weight / (self.height ** 2), 2)

    def interpret_bmi(self, bmi: float) -> str: # funkcja do interpretacji wyniku BMI
        if bmi < 18.5:
            return "niedowaga"
        elif 18.5 <= bmi <= 24.9:
            return "waga prawidlowa"
        elif 25.0 <= bmi <= 29.9:
            return "nadwaga"
        else:
            return "otylosc"
