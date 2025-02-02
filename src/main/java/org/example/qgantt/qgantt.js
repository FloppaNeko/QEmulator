function printTable(text) {
    let json = JSON.parse(text);
    
    let table = document.createElement('table');
    table.id = "root_table";

    for (let i = 0; i < json.rows + 1; ++i) {
        let row = table.insertRow();
        for (let j = 0; j < json.columns + 1; ++j) {
            row.insertCell();
        }
    }

    table.rows[0].cells[0].innerHTML = "$\\text{" + json.title + "}$";

    let terms = json.terms;
    for (let term of terms) {
        let row = term.row;
        let col = term.col;

        let amp = term.amp;
        let len = term.kets.length;

        let term_table = document.createElement('table');
        term_table.insertRow();

        let cell = term_table.rows[0].insertCell();
        cell.innerHTML = "$$" + amp + "$$";
        alert(cell.innerHTML);
        for (let i = 0; i <  term.kets.length; ++i) {
            cell = term_table.rows[0].insertCell();

            cell.innerHTML = "$$ |" + term.kets[i].value + "\\rangle $$";
        }

        table.rows[row+1].cells[col+1].append(term_table);
    }

    document.body.append(table);
    MathJax.typeset();
}

fetch("/qgantt/test.json")
    .then(response => response.text())
    .then(text => {
        printTable(text);
    })
    .catch(error => console.error('Error loading JSON:', error));
