document.addEventListener('DOMContentLoaded', function() {
    const table = document.getElementById('sortableTable');
    const thead = table.querySelector('thead');
    const tbody = table.querySelector('tbody');
    let sortOrder = {};

    async function loadData() {
        try {
            const response = await fetch('https://jsonplaceholder.typicode.com/posts');
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const data = await response.json();
            displayData(data);
        } catch (error) {
            console.error('Failed to fetch data:', error);
        }
    }

    function displayData(data) {
       if (data.length === 0) {
          tbody.innerHTML = '<tr><td colspan="100">Нет данных</td></tr>';
         return;
     }

      // Генерируем заголовки
     const headers = Object.keys(data[0]);
      let headersHTML = '';
      headers.forEach(header => {
        headersHTML += `<th data-column="${header}">${header}</th>`;
      });
      thead.innerHTML = `<tr>${headersHTML}</tr>`;

      // Генерируем строки таблицы
      let rowsHTML = '';
      data.forEach(item => {
        rowsHTML += '<tr>';
        headers.forEach(header => {
          rowsHTML += `<td>${item[header] == null ? '' : item[header]}</td>`;
        });
        rowsHTML += '</tr>';
      });
      tbody.innerHTML = rowsHTML;

     const ths = table.querySelectorAll('thead th');
     ths.forEach(header => {
        header.addEventListener('click', function() {
          const column = this.getAttribute('data-column');
          sortData(column, data);
        });
     });
 }

   function sortData(column, originalData) {
      const rows = [...originalData];
        if (sortOrder[column] === 'asc') {
             sortOrder[column] = 'desc';
        } else {
            sortOrder[column] = 'asc';
        }

        rows.sort((rowA, rowB) => {
            const cellA = rowA[column];
            const cellB = rowB[column];

            if (typeof cellA === 'number') {
                if (sortOrder[column] === 'asc') {
                    return cellA - cellB;
                } else {
                   return cellB - cellA
                }
            }else if (typeof cellA === 'boolean') {
                const numA = cellA === true ? 1 : 0;
                const numB = cellB === true ? 1 : 0;

                 if (sortOrder[column] === 'asc') {
                    return numA - numB;
                 } else {
                   return numB - numA
                }
             }else {
                 if(sortOrder[column] === 'asc') {
                      return String(cellA).localeCompare(String(cellB));
                    } else {
                      return String(cellB).localeCompare(String(cellA));
                    }
                }
            });

      displayData(rows);
    }

    loadData();
});