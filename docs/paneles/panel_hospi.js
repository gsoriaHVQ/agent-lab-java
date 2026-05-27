const DATOS_HOSPITALIZACION = [
    ["21-MAY 00:39", 586331, "H3 347", 1189541, "X", "X", "-", "-", 0, " ", "27-MAY", "KBAUTISTA", "-", "-"],
    ["25-MAY 15:00", 590405, "H3 350", 1232458, "-", "X", "-", "X", 1, "REAL.", "28-MAY", "KBAUTISTA", "-", "-"],
    ["14-MAY 12:27", 578962, "H3 305 -B", 1250585, "X", "X", "X", "X", 0, " ", "28-MAY", "DACOSTA", "-", "-"],
    ["26-MAY 13:55", 591556, "H3 304 -B", 1138224, "X", "X", "-", "-", 0, " ", "28-MAY", "DACOSTA", "-", "-"],
    ["26-MAY 12:02", 591347, "H3 312", 1254273, "-", "X", "-", "-", 1, " ", "28-MAY", "DANALUISA", "-", "-"],
    ["13-MAY 15:46", 577889, "H3 310", 1076372, "X", "X", "X", "X", 3, " ", "29-MAY", "DANALUISA", "-", "-"],
    ["27-MAY 07:19", 592241, "RECUPERACION 1", 1186635, "-", "-", "-", "-", 0, "AGEN.", " ", "SMEDIAVILLA", "-", "-"],
    ["21-MAY 11:59", 587091, "H2 217", 558033, "X", "X", "X", "-", 1, "REAL.", "27-MAY", " ", "-", "-"],
    ["26-MAY 12:18", 591388, "H2 220-B", 759527, "-", "X", "X", "-", 0, " ", "28-MAY", " ", "-", "-"],
    ["26-MAY 06:08", 590499, "H2 219-A", 1011088, "-", "X", "X", "-", 1, " ", "29-MAY", " ", "-", "-"],
    ["27-MAY 07:08", 592225, "CUBICULO 1", 863140, "-", "-", "-", "-", 0, "AGEN.", "27-MAY", " ", "-", "-"],
    ["26-MAY 03:28", 590494, "H2 206-A", 1259848, "-", "X", "-", "-", 0, " ", "28-MAY", " ", "-", "-"],
    ["23-MAY 20:25", 589818, "H2 212", 1034239, "X", "X", "X", "-", 2, " ", "02-JUN", " ", "-", "-"]
].map(function (row) {
    return {
        fecha: row[0],
        cd_atencion: row[1],
        habitacion: row[2],
        cd_paciente: row[3],
        exi: row[4],
        exl: row[5],
        cardio: row[6],
        fisio: row[7],
        interconsulta: row[8],
        cirugia: row[9],
        fecha_alta: row[10],
        enfermera: row[11],
        pintar: row[12],
        alta: row[13]
    };
});

function cargarTabla() {
    const sector = document.getElementById('sector').value;
    if (!sector) {
        return;
    }

    document.getElementById('titulo-panel').textContent = document.getElementById('sector').selectedOptions[0].textContent;

    if ($.fn.DataTable.isDataTable('#tabla-unidad')) {
        $('#tabla-unidad').DataTable().clear().destroy();
    }
    $('#tabla-unidad tbody').empty();

    DATOS_HOSPITALIZACION.forEach(function (unidad) {
        let color;
        if (unidad.alta === 'X') {
            color = 'fila-alta';
        } else if (unidad.pintar === 'X') {
            color = 'fila-pintar';
        } else {
            color = 'fila-normal';
        }

        const fila = $(`
            <tr>
                <td class="${color}">${unidad.fecha || ''}</td>
                <td class="${color}">${unidad.cd_atencion || ''}</td>
                <td class="${color}">${unidad.habitacion || ''}</td>
                <td class="${color}">${unidad.cd_paciente || ''}</td>
                ${unidad.exi === 'X'
                    ? `<td class="${color}"><i class="material-symbols-outlined icono-tabla">radiology</i></td>`
                    : `<td class="${color}"></td>`}
                ${unidad.exl === 'X'
                    ? `<td class="${color}"><i class="material-symbols-outlined icono-tabla">science</i></td>`
                    : `<td class="${color}"></td>`}
                ${unidad.cardio === 'X'
                    ? `<td class="${color}"><i class="material-symbols-outlined icono-tabla">cardiology</i></td>`
                    : `<td class="${color}"></td>`}
                ${unidad.fisio === 'X'
                    ? `<td class="${color}"><i class="material-symbols-outlined icono-tabla">accessibility_new</i></td>`
                    : `<td class="${color}"></td>`}
                ${unidad.interconsulta >= 1
                    ? `<td class="${color}"><i class="material-symbols-outlined icono-tabla">clinical_notes</i></td>`
                    : `<td class="${color}"></td>`}
                <td class="${color}">${unidad.cirugia || ''}</td>
                <td class="${color}">${unidad.fecha_alta || ''}</td>
                <td class="${color}">${unidad.enfermera || ''}</td>
            </tr>
        `);
        $('#tabla-unidad tbody').append(fila);
    });

    $('#tabla-unidad').DataTable({
        info: false,
        searching: false,
        responsive: true,
        scrollCollapse: true,
        paging: true,
        pageLength: 10,
        scrollY: '100vh',
        order: [[2, 'asc']],
        language: {
            processing: "Procesando...",
            search: "Buscar:",
            lengthMenu: "Mostrar _MENU_ registros",
            info: "_TOTAL_ registros encontrados",
            infoEmpty: "Ningun registro encontrado.",
            infoFiltered: "(filtrado de _MAX_ registros totales)",
            loadingRecords: "Cargando registros...",
            zeroRecords: "No se encontraron registros",
            emptyTable: "No hay datos disponibles en la tabla",
            paginate: {
                previous: "Ant.",
                next: "Sig."
            }
        }
    });
}

function cambiarPagina() {
    if (!$.fn.DataTable.isDataTable('#tabla-unidad')) {
        return;
    }

    const tabla = $('#tabla-unidad').DataTable();
    const info = tabla.page.info();
    if (info.pages <= 1) {
        return;
    }

    if (info.page < info.pages - 1) {
        tabla.page('next').draw('page');
    } else {
        tabla.page('first').draw('page');
    }
}

function toggleFullScreen() {
    if (!document.fullscreenElement) {
        document.documentElement.requestFullscreen();
    } else if (document.exitFullscreen) {
        document.exitFullscreen();
    }
}

function actualizarFechaHora() {
    const ahora = new Date();
    document.getElementById('fecha-hora').textContent = new Intl.DateTimeFormat('es-EC', {
        dateStyle: 'full',
        timeStyle: 'medium'
    }).format(ahora);
}

document.addEventListener('DOMContentLoaded', function () {
    actualizarFechaHora();
    cargarTabla();
    setInterval(actualizarFechaHora, 1000);
    setInterval(cargarTabla, 60000);
    setInterval(cambiarPagina, 20000);
});
