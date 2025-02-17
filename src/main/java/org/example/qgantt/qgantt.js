function setupTable(rows, columns, term_lengths) {
    let table = document.createElement('table');
    table.id = "root_table";

    let top_row = table.insertRow();
    top_row.id = "top_row";

    let title_cell = top_row.insertCell();
    title_cell.id = "title_cell";

    let arrow_cells_width = 0;
    for (let i = 0; i < columns; ++i) {
        if (i > 0) {
            top_row.insertCell();
        }

        let number_cell = top_row.insertCell();
        number_cell.setAttribute("colspan", 3 * term_lengths[i] + 1);
        number_cell.innerHTML = `$ ${i+1} $`;
    }

    let operation_row = table.insertRow();
    operation_row.className = "operations_row";

    operation_row.insertCell();

    for (let i = 0; i < columns; ++i) {
        if (i > 0) {
            operation_row.insertCell();
        }

        let operation_cell = operation_row.insertCell();
        operation_cell.className = "operation_cell";
        operation_cell.setAttribute("colspan", 3 * term_lengths[i] + 1);
        operation_cell.id = `operation_cell_${i}`; 
    }

    for (let i = 0; i < rows; ++i) {
        let terms_row = table.insertRow();
        terms_row.className = "terms_row";

        let row_number_cell = terms_row.insertCell();
        row_number_cell.innerHTML = `$\\texttt{${i+1}}$`;

        for (let j = 0; j < columns; ++j) {
            if (i == 0 && j > 0) {
                let arrow_block = terms_row.insertCell();
                arrow_block.className = "arrow_block";
                arrow_block.id = `arrow_block_${j-1}`;
                arrow_block.setAttribute("rowspan", rows);
                arrow_cells_width += 1;
            }

            let amp_cell = terms_row.insertCell();
            amp_cell.id = `cell_${i}_${j}_amp`;
            amp_cell.className = "amplitude";
            arrow_cells_width += 1;

            for (let k = 0; k < term_lengths[j]; ++k) {
                let bra_cell = terms_row.insertCell();
                bra_cell.innerHTML = "$|$";
                bra_cell.classList.add("braket");
                bra_cell.classList.add(`braket_${i}_${j}`);

                let terms_cell = terms_row.insertCell();
                terms_cell.id = `cell_${i}_${j}_${k}`;

                let ket_cell = terms_row.insertCell();
                ket_cell.innerHTML = "$\\rangle$";
                ket_cell.classList.add("braket");
                ket_cell.classList.add(`braket_${i}_${j}`);

                if (i == 0) {
                    arrow_cells_width += 3;
                }
            }
        }
    }

    let time_row = table.insertRow();
    
    let time_cell = time_row.insertCell();
    time_cell.innerHTML = "$\\text{time}$";

    let time_arrow_cell = time_row.insertCell();
    time_arrow_cell.setAttribute("colspan", arrow_cells_width);
    time_arrow_cell.id = "time_arrow_cell";

    return table;
}


function printTable(json) {
    let table = setupTable(json.rows, json.columns, json.term_lengths);

    table.querySelector("#title_cell").innerHTML = `$\\text{${json.title}}$`;

    let terms = json.terms;
    for (let term of terms) {
        let row = term.row;
        let col = term.col;

        let amp = term.amp;

        let amp_cell = table.querySelector(`#cell_${row}_${col}_amp`);
        amp_cell.innerHTML = `$ ${amp} $`;

        // let cell = table.querySelector(`#cell_${row}_${col}`)

        // let cell_text = "";
        // cell_text += amp.replace("\\frac", "\\tfrac");
        for (let i = 0; i <  term.kets.length; ++i) {
            let ket = term.kets[i];

            let ket_text = ket.value;

            let overset = false;
            if ("title" in ket) {
                let ket_title = ket.title;

                if ("title_style" in ket) {
                    if (ket.title_style == "bold") {
                        ket_title = `\\textbf{${ket_title}}`;
                    } else if (ket.title_style == "italic") {
                        ket_title = `\\textit{${ket_title}}`;
                    }
                } else {
                    ket_title = `\\text{${ket_title}}`;
                }

                ket_text = `\\overset{${ket_title}}{${ket_text}}`;
                overset = true;
            }

            let val_cell = table.querySelector(`#cell_${row}_${col}_${i}`);
            val_cell.innerHTML = `$ ${ket_text} $`;
            if (overset) {
                val_cell.classList.add("overset");
            }

            for (let elem of table.querySelectorAll(`td.braket_${row}_${col}`)) {
                elem.classList.add("visible");
            }
        }

    }

    let operations = json.operations;
    for (let op of operations) {
        let col = op.col;
        let text = op.text;

        let cell = table.querySelector(`#operation_cell_${col}`);
        cell.innerHTML = `$ ${text} $`;
    }

    document.body.append(table);
}

function drawArrows(json) {
    const cell_height = 41;

    let arrows = json.arrows;
    let columns = json.columns;

    for (let i = 0; i < arrows.length; ++i) {
        let script_text = "\\begin{tikzpicture}\n";

        for (let [s, e] of arrows[i]) {
            let begin_y = (columns - s - 1) * cell_height;
            let end_y = (columns - e - 1) * cell_height;

            script_text += `\\draw [thick, dotted, ->] (0pt, ${begin_y}pt) -- (30pt, ${end_y}pt);\n`;
        }

        script_text += "\\end{tikzpicture}";

        let arrow_block = document.querySelector(`#arrow_block_${i}`);

        let arrow_block_script = document.createElement("script");
        arrow_block_script.setAttribute("type", "text/tikz");
        arrow_block_script.innerHTML = script_text;
        arrow_block.append(arrow_block_script);
        //process_tikz(arrow_block_script);
    }

    let time_arrow_cell = document.querySelector("#time_arrow_cell");

    let time_arrow_script = document.createElement("script");
    time_arrow_script.setAttribute("type", "text/tikz");
    let width = Math.floor(time_arrow_cell.offsetWidth * 0.75);
    time_arrow_script.innerHTML = `
        \\begin{tikzpicture}
        \\draw [thick, dotted, ->] (0pt, 5pt) -- (${width}pt, 5pt);
        \\end{tikzpicture}
    `;
    time_arrow_cell.append(time_arrow_script);
    //process_tikz(time_arrow_script);
}

async function run() {
    try {
        // Fetch the JSON file
        const response = await fetch("/qgantt/test.json");
        const text = await response.text();
        const json = JSON.parse(text);
    
        printTable(json);
    
        // Wait for MathJax to finish typesetting
        await MathJax.typesetPromise();
    
        // Draw arrows after everything else is done
        drawArrows(json);
      
    } catch (error) {
      console.error("Error fetching or processing data:", error);
    }
}

run();
