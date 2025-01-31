document.addEventListener('DOMContentLoaded', function() {
    const table = document.getElementById('sortableTable');
    const headers = table.querySelectorAll('thead th');
    let sortOrder = {}; // Объект для хранения порядка сортировки для каждого столбца

    headers.forEach(header => {
        header.addEventListener('click', function() {
            const column = this.getAttribute('data-column');
            sortData(column);
        });
    });

    function sortData(column) {
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));

        // Инвертируем порядок сортировки, если кликаем по столбцу повторно
        if (sortOrder[column] === 'asc') {
            sortOrder[column] = 'desc';
        } else {
            sortOrder[column] = 'asc';
        }

        rows.sort((rowA, rowB) => {
           const cellA = rowA.querySelector(`td:nth-child(${Array.from(headers).findIndex(header => header.getAttribute('data-column') === column) + 1})`).textContent;
            const cellB = rowB.querySelector(`td:nth-child(${Array.from(headers).findIndex(header => header.getAttribute('data-column') === column) + 1})`).textContent;

           if (column === 'age') {
                const numA = parseInt(cellA);
                const numB = parseInt(cellB);

                 if (sortOrder[column] === 'asc') {
                     return numA - numB;
                 } else {
                    return numB - numA
                 }

            }else {
                if(sortOrder[column] === 'asc') {
                  return cellA.localeCompare(cellB);
                } else {
                    return cellB.localeCompare(cellA);
                }
            }
        });

        // Перерисовываем таблицу
        tbody.innerHTML = ''; // Очищаем таблицу
        rows.forEach(row => tbody.appendChild(row)); // добавляем отсортированные строки обратно
    }
});