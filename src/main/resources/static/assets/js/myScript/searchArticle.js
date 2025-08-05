$(document).ready(function() {
    // Recherche instantanée avec debounce
    $('#searchInput').on('input', function() {
        performSearch();
    });

    $('#searchType').change(performSearch);

    function performSearch() {
        const searchTerm = $('#searchInput').val().trim();
        const searchType = $('#searchType').val();

        const csrfToken = $("meta[name='_csrf']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // Afficher un indicateur de chargement
        $('#article-table tbody').html('<tr><td colspan="3" class="text-center"><div class="spinner-border text-primary" role="status"></div></td></tr>');

        $.ajax({
            type: "POST",
            url: "/article/search",
            contentType: "application/x-www-form-urlencoded",
            data: {
                query: searchTerm,
                searchType: searchType
            },
            success: function(data) {
                renderTable(data);
            },
            error: function(xhr) {
                console.error("Erreur lors de la recherche:", xhr.responseText);
                $('#article-table tbody').html('<tr><td colspan="3" class="text-center text-danger">Erreur lors de la recherche</td></tr>');
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // Ajout du header CSRF
            },

        });
    }

    function renderTable(data) {
        const tbody = $('#article-table tbody');
        tbody.empty();

        if (!data || data.length === 0) {
            tbody.append('<tr><td colspan="3" class="text-center">Aucun résultat trouvé</td></tr>');
            return;
        }

        data.forEach(article => {
            tbody.append(`
                <tr>
                    <td>${article.codeArticle || ''}</td>
                    <td>${article.designation || ''}</td>
                    <td>${article.udm.acronyme || ''}</td>
                    <td>
                        <a href="/article/edit/${article.idArticle}" class="btn btn-action edit">
                            <i class="bi bi-pencil"></i>
                        </a>
                    </td>
                </tr>
            `);
        });
    }

});