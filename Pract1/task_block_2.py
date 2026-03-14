# 3. Реалізуйте програму, яка визначає, чи є введене користувачем число простим

import math

def is_prime(number):
    if number < 2:
        return False

    if number == 2:
        return True

    if number % 2 == 0:
        return False

    limit = int(math.sqrt(number)) + 1
    for i in range(3, limit, 2):
        if number % i == 0:
            return False

    return True

def main():
    user_input = input("Введіть ціле число: ").strip()

    if user_input.isdigit() or (user_input.startswith('-') and user_input[1:].isdigit()):
        n = int(user_input)

        if is_prime(n):
            print(f"Число {n} є ПРОСТИМ")
        else:
            print(f"Число {n} НЕ є простим")
    else:
        print("Помилка: введіть ціле число")


main()