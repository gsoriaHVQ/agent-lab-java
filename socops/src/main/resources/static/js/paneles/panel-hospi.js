function cargarTabla() {
    const sector = $('#sector').val();
    if (!sector) {
        return;
    }

    document.getElementById('titulo-panel').textContent = $('#sector option:selected').text();

    $.ajax({
        url: '/paneles/panel_tabla',
        type: 'POST',
        data: {
            sector: sector
        },
        success: function (data) {
            if ($.fn.DataTable.isDataTable('#tabla-unidad')) {
                $('#tabla-unidad').DataTable().clear().destroy();
            }
            $('#tabla-unidad tbody').empty();

            $.each(data, function (i, unidad) {
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
                        ${unidad.exi === 'X' ?
                            `<td class="${color}"><i class="material-symbols-outlined icono-tabla">radiology</i></td>` :
                            `<td class="${color}"></td>`
                        }
                        ${unidad.exl === 'X' ?
                            `<td class="${color}"><i class="material-symbols-outlined icono-tabla">science</i></td>` :
                            `<td class="${color}"></td>`
                        }
                        ${unidad.cardio === 'X' ?
                            `<td class="${color}"><i class="material-symbols-outlined icono-tabla">cardiology</i></td>` :
                            `<td class="${color}"></td>`
                        }
                        ${unidad.fisio === 'X' ?
                            `<td class="${color}"><i class="material-symbols-outlined icono-tabla">accessibility_new</i></td>` :
                            `<td class="${color}"></td>`
                        }
                        ${unidad.interconsulta >= 1 ?
                            `<td class="${color}"><i class="material-symbols-outlined icono-tabla">clinical_notes</i></td>` :
                            `<td class="${color}"></td>`
                        }
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
        },
        error: function (xhr) {
            const errorMessage = (xhr.responseJSON && xhr.responseJSON.error)
                ? xhr.responseJSON.error
                : "No se pudo procesar la solicitud";
            console.error(errorMessage);
        }
    });
}

setInterval(cargarTabla, 60000);
setInterval(cambiarPagina, 20000);

function toggleFullScreen() {
    if (!document.fullscreenElement) {
        document.documentElement.requestFullscreen();
    } else if (document.exitFullscreen) {
        document.exitFullscreen();
    }
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
