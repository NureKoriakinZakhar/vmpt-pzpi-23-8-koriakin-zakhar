function filterJSONData(jsonInput, filterKey, filterValue) {
    try {
        const data = JSON.parse(jsonInput);

        const filteredData = data.filter(item => item[filterKey] === filterValue);

        const resultJSON = JSON.stringify(filteredData, null, 2);
        
        console.log("Відфільтровані дані:\n", resultJSON);
        
        return resultJSON;

    } catch (error) {
        console.error("Помилка:", error.message);
    }
}

const usersJSON = `[
    {"id": 1, "name": "Іван", "role": "admin"},
    {"id": 2, "name": "Олена", "role": "user"},
    {"id": 3, "name": "Петро", "role": "user"},
    {"id": 4, "name": "Марія", "role": "admin"}
]`;

filterJSONData(usersJSON, "role", "admin");