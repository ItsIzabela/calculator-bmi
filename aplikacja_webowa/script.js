function calculateBMI() {
    height_input = document.getElementById("height").value;
    weight_input = document.getElementById("weight").value;
    result = document.getElementById("result");

    height = parseFloat(height_input);
    weight = parseFloat(weight_input);

    if (!height || !weight || height <= 0 || weight <= 0) {
        alert("Wprowadź poprawne wartości!");
        return;
    }
    
    BMI = (weight / ((height / 100) ** 2)).toFixed(2);

    var text = "";
    var color = "";

    // Interpretacja wyniku
    if (18.5 <= BMI && BMI <= 24.9) {
        text = "Twoje BMI wynosi: " + BMI + " - waga prawidłowa";
        color = "green";
    } else if (BMI < 18.5) {
        text = "Twoje BMI wynosi: " + BMI + " - niedowaga";
        color = "yellow";
    } else if (25.0 <= BMI && BMI <= 29.9) {
        text = "Twoje BMI wynosi: " + BMI + " - nadwaga";
        color = "yellow";
    } else {
        text = "Twoje BMI wynosi: " + BMI + " - otyłość";
        color = "red";
    }

    result.innerHTML = text;
    result.style.backgroundColor = color;

    // Pobranie obecnej historii lub utworzenie pustej tablicy
    var history = JSON.parse(localStorage.getItem("bmiHistory")) || [];

    // Dodanie nowego wyniku na początek listy
    history.unshift(text);

    // Ograniczenie tablicy do maksymalnie 5 elementów
    if (history.length > 5) {
        history.pop();
    }

    // Zapisanie zaktualizowanej tablicy do localStorage
    localStorage.setItem("bmiHistory", JSON.stringify(history));

    // Odświeżenie widoku historii na stronie
    showHistory();
}

function showHistory() {
    var historyList = document.getElementById("history");
    var history = JSON.parse(localStorage.getItem("bmiHistory")) || [];

    historyList.innerHTML = "";

    for (var i = 0; i < history.length; i++) {
        var li = document.createElement("li");
        li.textContent = history[i];
        historyList.appendChild(li);
    }
}

// Wywołanie wyświetlenia historii przy załadowaniu strony
showHistory();