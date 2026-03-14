# 3. Створіть програму, яка приймає два числа від користувача та виводить їх суму

def calculateSum():
    try:
        num1 = float(input("Введіть перше число: ").replace(',', '.'))
        num2 = float(input("Введіть друге число: ").replace(',', '.'))

        result = num1 + num2

        print(f"Результат: {num1} + {num2} = {result:g}")

    except ValueError:
        print("Помилка: вводьте лише цифри")


calculateSum()