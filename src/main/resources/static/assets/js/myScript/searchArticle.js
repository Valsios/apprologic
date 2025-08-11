$(document).ready(function() {
    // Initialisation de DataTable
    const dataTable = $('#article-table').DataTable({
        pagingType: "simple_numbers",
        responsive: true,
        searching: false, // On désactive la recherche intégrée
        ordering: true,
        info: false,
        lengthMenu: [5, 10, 25, 50],
        language: {
            paginate: {
                previous: 'Précédent',
                next: 'Suivant'
            },
        },
        columns: [
            { data: 'codeArticle' },
            { data: 'designation' },
            { data: 'seuilMin' },
            {
                data: 'udm',
                render: function(data) {
                    return data?.acronyme || '';
                }
            },
            {
                data: null,
                orderable: false,
                render: function(data, type, row) {
                    return `<a href="/article/edit/${row.idArticle}" class="btn btn-action edit">
                                <i class="bi bi-pencil"></i>
                            </a>`;
                }
            }
        ],
        processing: true,
        serverSide: false
    });

    // Recherche instantanée avec debounce
    let searchTimeout;
    $('#searchInput').on('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(performSearch, 300);
    });

    $('#searchType').change(performSearch);

    function performSearch() {
        const searchTerm = $('#searchInput').val().trim();
        const searchType = $('#searchType').val();
        const csrfToken = $("meta[name='_csrf']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // Afficher le loading dans DataTable
        dataTable.processing(true);

        $.ajax({
            type: "POST",
            url: "/article/search",
            data: {
                query: searchTerm,
                searchType: searchType
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                // Formatage des données pour DataTables
                const formattedData = response.map(article => ({
                    codeArticle: article.codeArticle || '',
                    designation: article.designation || '',
                    seuilMin: article.seuilMin || '',
                    udm: article.udm || {},
                    idArticle: article.idArticle
                }));

                dataTable.clear().rows.add(formattedData).draw();
            },
            error: function(xhr) {
                console.error("Erreur lors de la recherche:", xhr.responseText);
                dataTable.clear().draw();
                dataTable.row.add(['', 'Erreur lors de la recherche', '', '', '']).draw();
            },
            complete: function() {
                dataTable.processing(false);
            }
        });
    }
});